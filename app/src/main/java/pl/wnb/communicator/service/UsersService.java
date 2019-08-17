package pl.wnb.communicator.service;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface UsersService {

    @GET("/onlineusers")
    Observable<ArrayList<String>> getOnlineUsers();
}
