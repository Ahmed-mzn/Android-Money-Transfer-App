package com.ahmed.bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.bank.api.model.AddFavori;
import com.ahmed.bank.api.model.Favorite;
import com.ahmed.bank.api.service.FavoriteAPI;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ValidationAddFavoritActivity extends AppCompatActivity {

    private EditText validateFavoriCarteLibelle;
    private EditText validateFavoriCarteNumero;
    private EditText validateFavoriComments;

    private Button validateFavoriBtn;

    private TextView validateFavoriBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation_add_favorit);


        validateFavoriCarteLibelle = findViewById(R.id.validateFavoriCarteLibelle);
        validateFavoriCarteNumero = findViewById(R.id.validateFavoriCarteNumero);
        validateFavoriComments = findViewById(R.id.validateFavoriComments);

        validateFavoriBtn = findViewById(R.id.validateFavoriBtn);

        validateFavoriBackToHome = findViewById(R.id.validateFavoriBackToHome);

        validateFavoriBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            validateFavoriCarteLibelle.setText(bundle.getString("libelle"));
            validateFavoriCarteNumero.setText(bundle.getString("numero"));
            validateFavoriComments.setText(bundle.getString("comments"));
        }

        validateFavoriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddFavori addFavori = new AddFavori(
                        Integer.parseInt(validateFavoriCarteNumero.getText().toString()),
                        validateFavoriComments.getText().toString());

                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(ValidationAddFavoritActivity.this);

                final FavoriteAPI favoriteAPI = retrofit.create(FavoriteAPI.class);

                Call<String> call = favoriteAPI.addFavorite(addFavori);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.code() == 201) {
                            showSnackBarSuccess("Transfer avec success");


                            Toast.makeText(ValidationAddFavoritActivity.this, "Favori ajoué avec success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ValidationAddFavoritActivity.this, ProfileActivity.class);
                            startActivity(intent);


                        } else if(response.code() == 200)  {
                            showSnackBarError("Already exist in favorites !");
                        } else {
                            showSnackBarError("Inforamtions incorrect ressayer");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

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