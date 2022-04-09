package com.ahmed.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmed.bank.api.model.DataTransferValidate;
import com.ahmed.bank.api.model.ValidateDataTransferResponse;
import com.ahmed.bank.api.service.DemandAPI;
import com.ahmed.bank.api.service.ValidateDataTransferAPI;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddDemandActivity extends AppCompatActivity {

    private EditText addDemandToCarteNumero;
    private EditText addDemandAmount;
    private EditText addDemandComments;

    private TextView addDemandBackToHome;

    private Button btnValidateDataDemand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_demand);

        addDemandToCarteNumero = findViewById(R.id.addDemandToCarteNumero);
        addDemandAmount = findViewById(R.id.addDemandAmount);
        addDemandComments = findViewById(R.id.addDemandComments);

        addDemandBackToHome = findViewById(R.id.addDemandBackToHome);

        btnValidateDataDemand = findViewById(R.id.btnValidateDataDemand);


        addDemandBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnValidateDataDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                if(addDemandToCarteNumero.getText().toString().trim().equals("") |
                        addDemandToCarteNumero.getText().toString().equals("")){
                    valid = false;
                    showSnackBarError("Please enter valid numero");
                    addDemandToCarteNumero.setError("Please enter valid numero");
                    addDemandToCarteNumero.requestFocus();
                } else if(addDemandAmount.getText().toString().trim().equals("") |
                        addDemandAmount.getText().toString().equals("")){
                    valid = false;
                    showSnackBarError("Please enter valid montant");
                    addDemandAmount.setError("Please enter valid montant");
                    addDemandAmount.requestFocus();
                } else if (addDemandComments.getText().toString().trim().equals("") |
                        addDemandComments.getText().toString().equals("")){
                    valid = false;
                    showSnackBarError("Please enter valid comments");
                    addDemandComments.setError("Please enter valid comments");
                    addDemandComments.requestFocus();
                }

                if(valid) {
                    DataTransferValidate dataTransferValidate = new DataTransferValidate(
                            Integer.parseInt(addDemandToCarteNumero.getText().toString()),
                            Integer.parseInt(addDemandAmount.getText().toString()));

                    Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(AddDemandActivity.this);
                    final DemandAPI demandAPI = retrofit.create(DemandAPI.class);

                    Call<ValidateDataTransferResponse> call = demandAPI.validateDemandData(dataTransferValidate);

                    call.enqueue(new Callback<ValidateDataTransferResponse>() {
                        @Override
                        public void onResponse(Call<ValidateDataTransferResponse> call, Response<ValidateDataTransferResponse> response) {
                            ValidateDataTransferResponse validateDataTransferResponse = response.body();
                            if(response.code() == 200) {

                                Intent intent = new Intent(AddDemandActivity.this, ValidationDemandActivity.class);
                                intent.putExtra("numero",String.valueOf(validateDataTransferResponse.getCarte().getNumero()));
                                intent.putExtra("libelle",validateDataTransferResponse.getCarte().getLibelle());
                                intent.putExtra("amount",addDemandAmount.getText().toString());
                                intent.putExtra("comments",addDemandComments.getText().toString());

                                startActivity(intent);

                            } else if(response.code() == 400)  {
                                showSnackBarError("Inforamtions incorrect ressayer");
                            } else {
                                showSnackBarError("Something wrong please try again");
                            }
                        }

                        @Override
                        public void onFailure(Call<ValidateDataTransferResponse> call, Throwable t) {
                            showSnackBarError("Something wrong please try again");
                        }
                    });
                }
            }
        });
    }
    private void showSnackBarError(String msg) {

        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
                .setTextColor(getColor(R.color.white))
                .setBackgroundTint(getColor(R.color.danger))
                .show();
    }
}