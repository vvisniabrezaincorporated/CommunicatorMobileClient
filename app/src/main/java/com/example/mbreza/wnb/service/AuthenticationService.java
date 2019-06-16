package com.example.mbreza.wnb.service;

import com.example.mbreza.wnb.model.Response;
import com.example.mbreza.wnb.model.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthenticationService {

    @FormUrlEncoded
    @POST("/login")
    Observable<Response> postSignIn(@Field("username") String username, @Field("password") String password);

    @POST("/register")
    Observable<Response> postSignUp(@Body User body);
}
