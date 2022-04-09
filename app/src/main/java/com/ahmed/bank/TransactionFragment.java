package com.ahmed.bank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ahmed.bank.api.model.Transaction;
import com.ahmed.bank.api.service.TransactionAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SharedPreferences sharedPreferences;
    private List<Transaction> transactions;
    private RecyclerView transactionRecyclerView;
    private FloatingActionButton fabAddTransactions;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_transaction, container, false);


        sharedPreferences = this.getActivity().getSharedPreferences("bank_app", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "null");


        transactionRecyclerView = (RecyclerView) rootView.findViewById(R.id.transactionRecView);
        fabAddTransactions = rootView.findViewById(R.id.fabAddTransactions);
        transactions = new ArrayList<Transaction>();

        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(getContext());

        final TransactionAPI transactionAPI = retrofit.create(TransactionAPI.class);

        Call<List<Transaction>> call = transactionAPI.getTransactions();

        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if(response.code() != 200) {
                    Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                } else {
                    List<Transaction> transactionsData = response.body();

                    for(Transaction transaction: transactionsData) {
                        transactions.add(transaction);
                    }

                    initData(transactions);
                }

            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });

        fabAddTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddTransferActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void initData(List<Transaction> transactions) {
        TransactionRecViewAdapter transactionRecViewAdapter = new TransactionRecViewAdapter(transactions,getContext());

        transactionRecyclerView.setAdapter(transactionRecViewAdapter);
        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}