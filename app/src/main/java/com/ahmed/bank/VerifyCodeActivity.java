package com.ahmed.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chaos.view.PinView;
import com.google.android.material.snackbar.Snackbar;

public class VerifyCodeActivity extends AppCompatActivity {

    private PinView otpCode;
    private Button verifyCodeBtn;

    private SharedPreferences sharedPreferences;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        otpCode = findViewById(R.id.otpCode);
        verifyCodeBtn = findViewById(R.id.verifyCodeBtn);

        sharedPreferences = getApplicationContext().getSharedPreferences("bank_app", Context.MODE_PRIVATE);
        code = sharedPreferences.getString("code", "null");

        verifyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.equals(otpCode.getText().toString())){
                    Intent intent = new Intent(VerifyCodeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showSnackBarError();
                }
            }
        });
    }

    private void showSnackBarError() {
        Snackbar.make(findViewById(android.R.id.content), "Invalid code", Snackbar.LENGTH_SHORT)
                .setTextColor(getColor(R.color.white))
                .setBackgroundTint(getColor(R.color.danger))
                .show();
    }
}