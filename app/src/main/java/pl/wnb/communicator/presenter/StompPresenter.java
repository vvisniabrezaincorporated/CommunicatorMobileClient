package pl.wnb.communicator.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.wnb.communicator.model.SqlUser;
import pl.wnb.communicator.model.service.UsersService;
import pl.wnb.communicator.model.sqlite.MessageManager;
import pl.wnb.communicator.model.sqlite.UserManager;
import pl.wnb.communicator.model.util.APIClientUtil;
import pl.wnb.communicator.model.util.ContextUtil;
import pl.wnb.communicator.model.util.EncryptionUtil;
import pl.wnb.communicator.model.util.GlobalUserUtil;
import pl.wnb.communicator.model.util.StompUtil;
import pl.wnb.communicator.view.ChatActivity;
import pl.wnb.communicator.view.TabbedActivity;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class StompPresenter {

    private View view;
    private StompClient stompClient;
    private static final SecureRandom SECURE_RANDOM;
    private EncryptionUtil encryptionUtil;
    private GlobalUserUtil globalUserUtil = GlobalUserUtil.getInstance();
    private MessageManager messageManager = new MessageManager();
    private UserManager userManager = new UserManager();

    static {
        try {
            SECURE_RANDOM = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not initialize a strong secure random instance", e);
        }
    }

    public StompPresenter(View view) {
        this.view = view;
        stompClient = Stomp.over(
                Stomp.ConnectionProvider.OKHTTP,
                "wss://10.0.2.2:8443/ws/websocket",
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
                try {
                    ChatActivity chatActivity = (ChatActivity) GlobalUserUtil.getInstance().getCurrentActivity();
                    if (chatActivity.getName().equals(jsonObject.getString("sender"))) {
                        view.showNotify(jsonObject.getString("sender") + " disconnected from chat.");
                        chatActivity.redirect(TabbedActivity.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            view.updateList();
        });
    }

    @SuppressLint("CheckResult")
    public void subscribeToMyTopic(String name) {
        GlobalUserUtil globalUserUtil = GlobalUserUtil.getInstance();

        if (globalUserUtil.getMyTopic() != null) {
            globalUserUtil.getMyTopic().dispose();
        }
        globalUserUtil.setMyTopic(stompClient.topic("/user/" + globalUserUtil.getName() + "/msg").subscribe(stompMessage -> {
            JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
            encryptionUtil = new EncryptionUtil(SECURE_RANDOM);
            SqlUser sqlUser = userManager.getUser();

            getPublicKey(jsonObject.getString("from")).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (byte[] receiverKey) -> {
                                getEmail(jsonObject.getString("from")).subscribe(
                                        (String receiverEmail) -> {
                                            String unencryptedMessage = encryptionUtil.decryptAndVerify(
                                                    jsonObject.getString("answer").trim(),
                                                    sqlUser.getPassword().trim(),
                                                    EncryptionUtil.ArmoredKeyPair.of(
                                                            sqlUser.getPrivateKey().trim(),
                                                            sqlUser.getPublicKey().trim()
                                                    ),
                                                    receiverEmail.trim(),
                                                    new String(receiverKey, StandardCharsets.UTF_8).trim());
                                            if (jsonObject.getString("from").equals(name)) {
                                                view.showMessage(name, unencryptedMessage, false);
                                            } else {
                                                view.showNotify("You received message from " + jsonObject.getString("from"));
                                            }
                                            messageManager.addMessage(jsonObject.getString("from"), unencryptedMessage, 0);
                                        },
                                        (Throwable e) -> Log.e("Call", "onError - getEmail " + e.toString()),
                                        () -> Log.e("Call", "onComplete - getEmail")
                                );
                            },
                            (Throwable e) -> Log.e("Call", "onError - getPublicKey " + e.toString()),
                            () -> Log.e("Call", "onComplete - getPublicKey")
                    );
        }));
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

    @SuppressLint("CheckResult")
    public void sendMessage(String name, String message) {
        JSONObject jsonObject = new JSONObject();
        encryptionUtil = new EncryptionUtil(SECURE_RANDOM);
        SqlUser sqlUser = userManager.getUser();

        getPublicKey(name).subscribe(
                (byte[] receiverKey) -> {
                    getEmail(name).subscribe(
                            (String receiverEmail) -> {
                                String encryptedMessage = "I tried to send a message but failed to encrypt it.";
                                try {
                                    encryptedMessage = encryptionUtil.encryptAndSign(
                                            message.trim(),
                                            sqlUser.getEmail().trim(),
                                            sqlUser.getPassword().trim(),
                                            EncryptionUtil.ArmoredKeyPair.of(
                                                    sqlUser.getPrivateKey().trim(),
                                                    sqlUser.getPublicKey().trim()
                                            ),
                                            receiverEmail.trim(),
                                            new String(receiverKey, StandardCharsets.UTF_8).trim()
                                    );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    view.showNotify("Failed to encrypt message.");
                                }
                                try {
                                    jsonObject.put("userID", name);
                                    jsonObject.put("fromUserID", globalUserUtil.getName());
                                    jsonObject.put("message", encryptedMessage);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                stompClient.send("/chat", jsonObject.toString()).subscribe();
                                messageManager.addMessage(name, message.trim(), 1);
                                view.showMessage(globalUserUtil.getName(), message.trim(), true);
                            },
                            (Throwable e) -> Log.e("Call", "onError - getEmail " + e.toString()),
                            () -> Log.e("Call", "onComplete - getEmail")
                    );
                },
                (Throwable e) -> Log.e("Call", "onError - getPublicKey " + e.toString()),
                () -> Log.e("Call", "onComplete - getPublicKey")
        );
    }

    public void getOnlineUsers() {
        UsersService apiService = APIClientUtil.getClient().create(UsersService.class);
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
                        Log.e("Call", "onNext - getUsers " + users.size());
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

    public Observable<byte[]> getPublicKey(String name) {
        UsersService apiService = APIClientUtil.getClient().create(UsersService.class);
        Observable<byte[]> getPublicKeyObservable = apiService.getPublicKey(name);
        return getPublicKeyObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> getEmail(String name) {
        UsersService apiService = APIClientUtil.getClient().create(UsersService.class);
        Observable<String> getPublicKeyObservable = apiService.getEmail(name);
        return getPublicKeyObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public interface View {
        void showMessage(String name, String message, boolean myMsg);

        void showNotify(String message);

        void updateList();

        void redirect(Class myClass);
    }
}
