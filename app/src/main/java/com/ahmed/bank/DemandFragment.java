package com.ahmed.bank;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ahmed.bank.api.model.Demand;
import com.ahmed.bank.api.model.Transaction;
import com.ahmed.bank.api.service.DemandAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemandFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemandFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Demand> demands;
    private RecyclerView demandRecView;
    private FloatingActionButton fabAddDemand;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DemandFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DemandFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DemandFragment newInstance(String param1, String param2) {
        DemandFragment fragment = new DemandFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_demand, container, false);

        demandRecView = (RecyclerView) rootView.findViewById(R.id.demandRecView);
        fabAddDemand = rootView.findViewById(R.id.fabAddDemand);
        demands = new ArrayList<>();

        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(getContext());


        final DemandAPI demandAPI = retrofit.create(DemandAPI.class);

        Call<List<Demand>> call = demandAPI.getDemands();


        call.enqueue(new Callback<List<Demand>>() {
            @Override
            public void onResponse(Call<List<Demand>> call, Response<List<Demand>> response) {
                if(response.code() != 200) {
                    Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                } else {
                    List<Demand> demandList = response.body();

                    for(Demand demand : demandList) {
                        demands.add(demand);
                    }

                    initData(demands);
                }
            }

            @Override
            public void onFailure(Call<List<Demand>> call, Throwable t) {

            }
        });

        fabAddDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddDemandActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void initData(List<Demand> demands) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();

        DemandRecViewAdapter demandRecViewAdapter = new DemandRecViewAdapter(
                demands,
                getContext(),
                transaction
        );

        demandRecView.setAdapter(demandRecViewAdapter);
        demandRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}