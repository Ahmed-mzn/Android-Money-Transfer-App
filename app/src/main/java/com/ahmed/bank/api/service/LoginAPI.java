package com.ahmed.bank.api.service;

import com.ahmed.bank.api.model.Login;
import com.ahmed.bank.api.model.ResponseLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginAPI {

    @POST("login")
    Call<ResponseLogin> login(@Body Login login);
}
