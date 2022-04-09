package com.ahmed.bank.api.service;

import com.ahmed.bank.api.model.Transaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TransactionAPI {

    @GET("transactions")
    Call<List<Transaction>> getTransactions();
}
