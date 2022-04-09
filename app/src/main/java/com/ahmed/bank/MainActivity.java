package com.ahmed.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String token;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getApplicationContext().getSharedPreferences("bank_app", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "null");
        code = sharedPreferences.getString("code", "null");

            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent;
                    if(token != "null" && code != "null") {
                        intent = new Intent(MainActivity.this, VerifyCodeActivity.class);
                    } else {
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();

                }
            };
            handler.postDelayed(runnable, 3000);

    }
}