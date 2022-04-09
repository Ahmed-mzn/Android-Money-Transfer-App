package com.ahmed.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.bank.api.model.AddDemand;
import com.ahmed.bank.api.model.Demand;
import com.ahmed.bank.api.service.DemandAPI;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ValidationDemandActivity extends AppCompatActivity {

    private EditText validateDemandToCarteLibelle;
    private EditText validateDemandToCarteNumero;
    private EditText validateDemandAmount;
    private EditText validateDemandComments;

    private Button validateDemandBtn;

    private TextView validateDemandBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation_demand);

        validateDemandToCarteLibelle = findViewById(R.id.validateDemandToCarteLibelle);
        validateDemandToCarteNumero = findViewById(R.id.validateDemandToCarteNumero);
        validateDemandAmount = findViewById(R.id.validateDemandAmount);
        validateDemandComments = findViewById(R.id.validateDemandComments);

        validateDemandBtn = findViewById(R.id.validateDemandBtn);

        validateDemandBackToHome = findViewById(R.id.validateDemandBackToHome);

        validateDemandBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            validateDemandToCarteLibelle.setText(bundle.getString("libelle"));
            validateDemandToCarteNumero.setText(bundle.getString("numero"));
            validateDemandAmount.setText(bundle.getString("amount"));
            validateDemandComments.setText(bundle.getString("comments"));
        }

        validateDemandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDemand addDemand = new AddDemand(
                        Integer.parseInt(validateDemandToCarteNumero.getText().toString()),
                        Double.valueOf(validateDemandAmount.getText().toString()),
                        validateDemandComments.getText().toString()
                );

                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(ValidationDemandActivity.this);

                final DemandAPI demandAPI = retrofit.create(DemandAPI.class);

                Call<Demand> call = demandAPI.addDemand(addDemand);

                call.enqueue(new Callback<Demand>() {
                    @Override
                    public void onResponse(Call<Demand> call, Response<Demand> response) {
                        if(response.code() == 201) {
                            showSnackBarSuccess("Transfer avec success");
                            Toast.makeText(ValidationDemandActivity.this, "Demand transmis avec success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ValidationDemandActivity.this, ProfileActivity.class);
                            startActivity(intent);


                        } else if(response.code() == 400)  {
                            showSnackBarError("Inforamtions incorrect ressayer");
                        }
                    }

                    @Override
                    public void onFailure(Call<Demand> call, Throwable t) {

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