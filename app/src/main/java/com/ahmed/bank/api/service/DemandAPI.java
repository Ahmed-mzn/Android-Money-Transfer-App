package com.ahmed.bank.api.service;

import com.ahmed.bank.api.model.AddDemand;
import com.ahmed.bank.api.model.DataTransferValidate;
import com.ahmed.bank.api.model.Demand;
import com.ahmed.bank.api.model.Transaction;
import com.ahmed.bank.api.model.ValidateDataTransferResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DemandAPI {

    @GET("demands")
    Call<List<Demand>> getDemands();

    @POST("add_demand")
    Call<Demand> addDemand(@Body AddDemand addDemand);

    @POST("validate_demand_data")
    Call<ValidateDataTransferResponse> validateDemandData(@Body DataTransferValidate dataTransferValidate);

    @POST("refuse_demand/{id}")
    Call<String> refuseDemand(@Path("id") int id);

    @POST("accept_demand/{id}")
    Call<Transaction> acceptDemand(@Path("id") int id, @Body DataTransferValidate dataTransferValidate);
}
