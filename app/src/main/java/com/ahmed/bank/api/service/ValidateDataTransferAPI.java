package com.ahmed.bank.api.service;

import com.ahmed.bank.api.model.Carte;
import com.ahmed.bank.api.model.DataTransferValidate;
import com.ahmed.bank.api.model.Transaction;
import com.ahmed.bank.api.model.ValidateDataTransferResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ValidateDataTransferAPI {

    @POST("validate_transfer_data")
    Call<ValidateDataTransferResponse> validateDataTransfer(@Body DataTransferValidate dataTransferValidate);

    @POST("add_transfer")
    Call<Transaction> addTransfer(@Body DataTransferValidate dataTransferValidate);
}
