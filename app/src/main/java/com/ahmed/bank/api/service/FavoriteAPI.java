package com.ahmed.bank.api.service;

import com.ahmed.bank.api.model.AddFavori;
import com.ahmed.bank.api.model.Favorite;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoriteAPI {

    @GET("favorites")
    Call<List<Favorite>> getFavorites();

    @POST("add_favorite")
    Call<String> addFavorite(@Body AddFavori addFavori);

    @DELETE("favorites/{id}/")
    Call<String> deleteFavorite(@Path("id") int id);
}
