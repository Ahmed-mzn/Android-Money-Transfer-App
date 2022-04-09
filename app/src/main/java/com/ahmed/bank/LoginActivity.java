package com.ahmed.bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ahmed.bank.api.model.Login;
import com.ahmed.bank.api.model.ResponseLogin;
import com.ahmed.bank.api.service.LoginAPI;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private EditText numeroCarte;
    private EditText codeCarte;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.loginBtn);
        numeroCarte = findViewById(R.id.numeroCarte);
        codeCarte = findViewById(R.id.codeCarte);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Login login = new Login(Integer.parseInt(numeroCarte.getText().toString()),
                        Integer.parseInt(codeCarte.getText().toString()));
                loginRequest(login);
            }
        });
    }

    private void loginRequest(Login login) {
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(this);

        final LoginAPI loginAPI = retrofit.create(LoginAPI.class);

        Call<ResponseLogin> call = loginAPI.login(login);

        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if(response.code() == 200){
                    sharedPreferences = getSharedPreferences("bank_app", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.putString("code", codeCarte.getText().toString());
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else if(response.code() == 400) {
                    showSnackBarError("Invalid carte ou code incorrect");
                } else {
                    showSnackBarError("Something wrong please try again");
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                showSnackBarError("Something wrong please try again");
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