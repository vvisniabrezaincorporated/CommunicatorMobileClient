package pl.wnb.communicator.presenter;

import android.util.Log;
import android.view.View;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.wnb.communicator.model.User;
import pl.wnb.communicator.service.UsersService;
import pl.wnb.communicator.util.APIClientUtil;

public class UserPresenter {

    private View view;

    public UserPresenter(View view) {
        this.view = view;
    }

    public void getAllUsers() {
        APIClientUtil api = new APIClientUtil();
        final UsersService apiService = api.getClient().create(UsersService.class);

        Observable<List<User>> getUsersObservable = apiService.getUsers();

        getUsersObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("Call", "onSubscribe - getUsers");
                    }
                    @Override
                    public void onNext(List<User> users) {
                        Log.e("Call", "onNext - getUsers");
                        for (User user : users) {
                            System.out.println(user.toString());
                        }
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
}
