package pl.wnb.communicator.presenter;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import pl.wnb.communicator.model.Response;
import pl.wnb.communicator.model.User;
import pl.wnb.communicator.service.AuthenticationService;
import pl.wnb.communicator.util.APIClientUtil;
import pl.wnb.communicator.view.HomeActivity;
import retrofit2.HttpException;

public class AuthenticationPresenter {

    private View view;
    private APIClientUtil api = new APIClientUtil();
    private AuthenticationService apiService = api.getClient().create(AuthenticationService.class);

    public AuthenticationPresenter(View view) {
        this.view = view;
    }

    public void signUp(User user){
        Observable<Response> postSignUpObservable = apiService.postSignUp(user);

        postSignUpObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("Call", "onSubscribe - logIn");
                    }
                    @Override
                    public void onNext(Response response) {
                        Log.e("Call", response.toString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        ResponseBody body = ((HttpException) e).response().errorBody();
                        Response response = null;
                        try {
                            response = new Gson().fromJson(body.string(), Response.class);
                            if(response.getStatus() == 401){
                                view.showNotify("Wrong username or password");
                            }else{
                                view.showNotify("Something went wrong, try again later.");
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        Log.e("Call","I am in error" + response.toString());
                    }
                    @Override
                    public void onComplete() {
                        Log.e("Call", "onComplete - logIn");
                    }
                });
    }

    public void signIn(String username, String password){
        Observable<Response> postSignInObservable = apiService.postSignIn(username, password);

        postSignInObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e("Call", "onSubscribe - logIn");
            }
            @Override
            public void onNext(Response response) {
                if(response.getStatus() == 200){
                    view.redirectHome(HomeActivity.class);
                }
                Log.e("Call", response.toString());
            }
            @Override
            public void onError(Throwable e) {
                ResponseBody body = ((HttpException) e).response().errorBody();
                Response response = null;
                try {
                    response = new Gson().fromJson(body.string(), Response.class);
                    if(response.getStatus() == 401){
                        view.showNotify("Wrong username or password");
                    }else{
                        view.showNotify("Something went wrong, try again later.");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Log.e("Call","I am in error" + response.toString());
            }
            @Override
            public void onComplete() {
                Log.e("Call", "onComplete - logIn");
            }
        });
    }

    public interface View {
        void showNotify(String info);
        void redirectHome(Class myClass);
    }
}
