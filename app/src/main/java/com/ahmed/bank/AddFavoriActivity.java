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
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddFavoriActivity extends AppCompatActivity {

    private EditText addFavoriCarteNumero;
    private EditText addFavoriComments;

    private Button btnValidateDataFavori;

    private TextView addFavoriBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favori);

        addFavoriCarteNumero = findViewById(R.id.addFavoriCarteNumero);
        addFavoriComments = findViewById(R.id.addFavoriComments);

        btnValidateDataFavori = findViewById(R.id.btnValidateDataFavori);

        addFavoriBackToHome = findViewById(R.id.addFavoriBackToHome);


        addFavoriBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnValidateDataFavori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                if(addFavoriCarteNumero.getText().toString().trim().equals("") |
                        addFavoriCarteNumero.getText().toString().equals("")){
                    valid = false;
                    showSnackBarError("Please enter valid numero");
                    addFavoriCarteNumero.setError("Please enter valid numero");
                    addFavoriCarteNumero.requestFocus();
                } else if (addFavoriComments.getText().toString().trim().equals("") |
                        addFavoriComments.getText().toString().equals("")){
                    valid = false;
                    showSnackBarError("Please enter valid comments");
                    addFavoriComments.setError("Please enter valid comments");
                    addFavoriComments.requestFocus();
                }

                if(valid) {
                    DataTransferValidate dataTransferValidate = new DataTransferValidate(
                            Integer.parseInt(addFavoriCarteNumero.getText().toString()),
                            0);

                    Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(AddFavoriActivity.this);
                    final DemandAPI demandAPI = retrofit.create(DemandAPI.class);

                    Call<ValidateDataTransferResponse> call = demandAPI.validateDemandData(dataTransferValidate);

                    call.enqueue(new Callback<ValidateDataTransferResponse>() {
                        @Override
                        public void onResponse(Call<ValidateDataTransferResponse> call, Response<ValidateDataTransferResponse> response) {
                            ValidateDataTransferResponse validateDataTransferResponse = response.body();
                            if(response.code() == 200) {

                                Intent intent = new Intent(AddFavoriActivity.this, ValidationAddFavoritActivity.class);
                                intent.putExtra("numero",String.valueOf(validateDataTransferResponse.getCarte().getNumero()));
                                intent.putExtra("libelle",validateDataTransferResponse.getCarte().getLibelle());
                                intent.putExtra("comments",addFavoriComments.getText().toString());

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