package com.ahmed.bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.bank.api.model.Carte;
import com.ahmed.bank.api.model.Profile;
import com.ahmed.bank.api.model.Transaction;
import com.ahmed.bank.api.service.ProfileAPI;
import com.ahmed.bank.api.service.TransactionAPI;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView profileLibelle;
    private TextView profileLibelle2;
    private TextView profileSolde;
    private TextView profileTransactionCount;
    private TextInputLayout editTextProfileLibelle;
    private TextInputLayout editTextProfileCin;
    private TextInputLayout editTextProfileNumero;
    private TextInputLayout editTextProfileValidUntil;

    private SharedPreferences sharedPreferences;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        profileLibelle = rootView.findViewById(R.id.profileLibelle);
        profileLibelle2 = rootView.findViewById(R.id.profileLibelle2);
        profileSolde = rootView.findViewById(R.id.profileSolde);
        profileTransactionCount = rootView.findViewById(R.id.profileTransactionCount);
        editTextProfileLibelle = rootView.findViewById(R.id.editTextProfileLibelle);
        editTextProfileCin = rootView.findViewById(R.id.editTextProfileCin);
        editTextProfileNumero = rootView.findViewById(R.id.editTextProfileNumero);
        editTextProfileValidUntil = rootView.findViewById(R.id.editTextProfileValidUntil);




        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(getContext());

        final ProfileAPI profileAPI = retrofit.create(ProfileAPI.class);

        Call<Profile> call = profileAPI.getProfile();

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(response.code() == 200 ) {

                    Profile profile = response.body();

                    sharedPreferences = HomeFragment.this.getContext().getSharedPreferences("bank_app", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putInt("carte_id", response.body().getId());
                    editor.apply();

                    profileLibelle.setText(profile.getLibelle());
                    profileLibelle2.setText(String.valueOf(profile.getNumero()));
                    profileSolde.setText(String.valueOf(profile.getSolde()));
                    profileTransactionCount.setText(String.valueOf(profile.getTransactionsCount()));
                    editTextProfileLibelle.getEditText().setText(profile.getLibelle());
                    editTextProfileNumero.getEditText().setText(String.valueOf(profile.getNumero()));
                    editTextProfileCin.getEditText().setText(profile.getCin());
                    editTextProfileValidUntil.getEditText().setText(profile.getValidateUntil());
                } else {
                    showSnackBarError("Login required", rootView);
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                showSnackBarError("Something wrong please try again", rootView);
            }
        });

        return rootView;
    }
    private void showSnackBarError(String msg, View rootView) {

        Snackbar.make(rootView.findViewById(R.id.parentProfileFragment), "Invalid carte ou code incorrect", Snackbar.LENGTH_SHORT)
                .setTextColor(getContext().getColor(R.color.white))
                .setBackgroundTint(getContext().getColor(R.color.danger))
                .show();
    }

}