package com.ahmed.bank.api.service;

import com.ahmed.bank.api.model.Profile;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProfileAPI {

    @GET("get_profile")
    Call<Profile> getProfile();
}
