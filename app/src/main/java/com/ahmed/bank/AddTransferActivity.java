package com.ahmed.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.bank.api.model.DataTransferValidate;
import com.ahmed.bank.api.model.Favorite;
import com.ahmed.bank.api.model.ValidateDataTransferResponse;
import com.ahmed.bank.api.service.FavoriteAPI;
import com.ahmed.bank.api.service.ValidateDataTransferAPI;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddTransferActivity extends AppCompatActivity {

    private EditText toCarteNumero;
    private EditText transferMontant;
    private TextView backToHome;
    private Button btnValidateDataTransfer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transfer);

        toCarteNumero = findViewById(R.id.toCarteNumero);
        transferMontant = findViewById(R.id.transferMontant);
        backToHome = findViewById(R.id.backToHome);



        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnValidateDataTransfer = findViewById(R.id.btnValidateDataTransfer);

        btnValidateDataTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                if(toCarteNumero.getText().toString().trim().equals("")){
                    valid = false;
                    showSnackBarError("Please enter valid numero");
                    toCarteNumero.setError("Please enter valid numero");
                    toCarteNumero.requestFocus();
                }

                if(transferMontant.getText().toString().trim().equals("")){
                    valid = false;
                    showSnackBarError("Please enter valid montant");
                    transferMontant.setError("Please enter valid montant");
                    transferMontant.requestFocus();
                }

                if(valid) {
                    DataTransferValidate dataTransferValidate = new DataTransferValidate(
                            Integer.parseInt(toCarteNumero.getText().toString()),
                            Integer.parseInt(transferMontant.getText().toString()));

                    Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(AddTransferActivity.this);
                    final ValidateDataTransferAPI validateDataTransferAPI = retrofit.create(ValidateDataTransferAPI.class);

                    Call<ValidateDataTransferResponse> call = validateDataTransferAPI.validateDataTransfer(dataTransferValidate);

                    call.enqueue(new Callback<ValidateDataTransferResponse>() {
                        @Override
                        public void onResponse(Call<ValidateDataTransferResponse> call, Response<ValidateDataTransferResponse> response) {
                            ValidateDataTransferResponse validateDataTransferResponse = response.body();
                            if(response.code() == 200) {

                                Intent intent = new Intent(AddTransferActivity.this, ValidationAddTransferActivity.class);
                                intent.putExtra("numero",String.valueOf(validateDataTransferResponse.getCarte().getNumero()));
                                intent.putExtra("libelle",validateDataTransferResponse.getCarte().getLibelle());
                                intent.putExtra("amount",transferMontant.getText().toString());

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