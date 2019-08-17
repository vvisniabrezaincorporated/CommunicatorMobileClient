package pl.wnb.communicator.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.wnb.communicator.service.UsersService;
import pl.wnb.communicator.util.APIClientUtil;
import pl.wnb.communicator.util.ContextUtil;
import pl.wnb.communicator.util.GlobalUserUtil;
import pl.wnb.communicator.util.StompUtil;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class StompPresenter {

    private View view;
    private StompClient stompClient;
    private GlobalUserUtil globalUserUtil = GlobalUserUtil.getInstance();

    public StompPresenter(View view) {
        this.view = view;
        stompClient = Stomp.over(
                Stomp.ConnectionProvider.OKHTTP,
                "wss://83.0.171.108:8443/ws/websocket",
                null,
                APIClientUtil.createClient(ContextUtil.getAppContext())
        );
        stompClient.connect();
        StompUtil.lifecycle(stompClient);
    }

    @SuppressLint("CheckResult")
    public void subscribeToGlobalTopic() {
        stompClient.topic("/online").subscribe(stompMessage -> {
            JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
            if (jsonObject.getString("type").equals("JOIN")) {
                if (!jsonObject.getString("sender").equals(globalUserUtil.getName())) {
                    globalUserUtil.addOnlineUser(jsonObject.getString("sender"));
                }
            } else if (jsonObject.getString("type").equals("LEAVE")) {
                globalUserUtil.removeOnlineUser(jsonObject.getString("sender"));
            }
            view.updateList();
            Log.e("subscribeToGlobalTopic", "Receive: " + jsonObject.toString());
        });
    }

    @SuppressLint("CheckResult")
    public void subscribeToMyTopic() {
        stompClient.topic("/user/" + globalUserUtil.getName() + "/msg").subscribe(stompMessage -> {
            JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
            view.showMessage("ppp", jsonObject.getString("answer"), false);
            Log.e("subscribeToMyTopic", "Receive: " + jsonObject.getString("answer"));
        });
    }

    public void notifyOnline() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sender", globalUserUtil.getName());
            jsonObject.put("type", "JOIN");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        stompClient.send("/app/chat.addUser", jsonObject.toString()).subscribe();
    }

    public void sendMessage(String name, String message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", name);
            jsonObject.put("fromUserID", globalUserUtil.getName());
            jsonObject.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("sendMessage", "Message" + name);
        stompClient.send("/chat", jsonObject.toString()).subscribe();
        view.showMessage(globalUserUtil.getName(), message, true);
    }

    public void getOnlineUsers() {
        final UsersService apiService = APIClientUtil.getClient().create(UsersService.class);
        Observable<ArrayList<String>> getUsersObservable = apiService.getOnlineUsers();
        getUsersObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("Call", "onSubscribe - getUsers");
                    }

                    @Override
                    public void onNext(ArrayList<String> users) {
                        Log.e("Call", "onNext - getUsers" + users.toString());
                        globalUserUtil.addAllOnlineUser(users);
                        view.updateList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Call", "onError - getUsers" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("Call", "onComplete - getUsers");
                    }
                });
    }

    public interface View {
        void showMessage(String name, String message, boolean myMsg);

        void updateList();
    }
}
