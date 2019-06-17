package pl.wnb.communicator.service;

import java.util.List;

import io.reactivex.Observable;
import pl.wnb.communicator.model.User;
import retrofit2.http.GET;

public interface  UsersService {

    @GET("/android/users/getall")
    Observable<List<User>> getUsers();
}
