package pl.wnb.communicator.service;

import io.reactivex.Observable;
import pl.wnb.communicator.model.Response;
import pl.wnb.communicator.model.User;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthenticationService {

    @FormUrlEncoded
    @POST("/android/androidlogin")
    Observable<Response> postSignIn(@Field("username") String username, @Field("password") String password);

    @POST("/register/new")
    Observable<Response> postSignUp(@Body User body);
}
