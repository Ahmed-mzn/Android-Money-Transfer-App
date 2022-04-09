package com.ahmed.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.bank.api.model.DataTransferValidate;
import com.ahmed.bank.api.model.Transaction;
import com.ahmed.bank.api.model.ValidateDataTransferResponse;
import com.ahmed.bank.api.service.ValidateDataTransferAPI;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ValidationAddTransferActivity extends AppCompatActivity {

    private EditText libelleCarteValidate;
    private EditText numeroCarteValidate;
    private EditText amountValidate;

    private Button validTransferBtn;

    private TextView goBackValidateTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation_add_transfer);

        libelleCarteValidate = findViewById(R.id.libelleCarteValidate);
        numeroCarteValidate = findViewById(R.id.numeroCarteValidate);
        amountValidate = findViewById(R.id.amountValidate);

        validTransferBtn = findViewById(R.id.validTransferBtn);

        goBackValidateTransfer = findViewById(R.id.goBackValidateTransfer);


        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            libelleCarteValidate.setText(bundle.getString("libelle"));
            numeroCarteValidate.setText(bundle.getString("numero"));
            amountValidate.setText(bundle.getString("amount"));
        }

        goBackValidateTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        validTransferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataTransferValidate dataTransferValidate = new DataTransferValidate(
                        Integer.parseInt(numeroCarteValidate.getText().toString()),
                        Integer.parseInt(amountValidate.getText().toString()));

                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(ValidationAddTransferActivity.this);
                final ValidateDataTransferAPI validateDataTransferAPI = retrofit.create(ValidateDataTransferAPI.class);

                Call<Transaction> call = validateDataTransferAPI.addTransfer(dataTransferValidate);

                call.enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                        if(response.code() == 201) {
                            showSnackBarSuccess("Transfer avec success");
                            Toast.makeText(ValidationAddTransferActivity.this, "Transfer avec success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ValidationAddTransferActivity.this, ProfileActivity.class);
                            startActivity(intent);


                        } else if(response.code() == 400)  {
                            showSnackBarError("Inforamtions incorrect ressayer");
                        }
                    }

                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {
                        showSnackBarError("Something wrong please try again");
                    }
                });
            }
        });
    }

    private void showSnackBarError(String msg) {

        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
                .setTextColor(getColor(R.color.white))
                .setBackgroundTint(getColor(R.color.danger))
                .show();
    }

    private void showSnackBarSuccess(String msg) {

        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
                .show();
    }
}