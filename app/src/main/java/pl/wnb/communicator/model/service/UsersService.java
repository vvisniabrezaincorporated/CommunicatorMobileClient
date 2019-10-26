package pl.wnb.communicator.model.service;

import java.util.ArrayList;

import io.reactivex.Observable;
import pl.wnb.communicator.model.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersService {

    @GET("/android/online")
    Observable<ArrayList<String>> getOnlineUsers();

    @POST("/android/user/{username}")
    Observable<Response> postPublicKey(@Path("username") String username, @Body byte[] publicKey);

    @GET("/android/user/{username}")
    Observable<byte[]> getPublicKey(@Path("username") String username);

    @POST("/android/email/{username}")
    Observable<Response> postEmail(@Path("username") String username, @Body String publicKeyEmail);

    @GET("/android/email/{username}")
    Observable<String> getEmail(@Path("username") String username);
}
