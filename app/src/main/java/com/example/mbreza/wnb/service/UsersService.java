package com.example.mbreza.wnb.service;

import com.example.mbreza.wnb.model.User;

import java.util.List;
import io.reactivex.Observable;

import retrofit2.http.GET;


public interface  UsersService {

    @GET("/android/users/getall")
    Observable<List<User>> getUsers();
}
