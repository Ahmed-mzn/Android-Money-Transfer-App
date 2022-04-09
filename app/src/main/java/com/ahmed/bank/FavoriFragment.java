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
import com.ahmed.bank.api.model.Favorite;
import com.ahmed.bank.api.service.FavoriteAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private List<Favorite> favorites;
    private RecyclerView favoriteRecView;
    private FloatingActionButton fabAddFavorite;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriFragment newInstance(String param1, String param2) {
        FavoriFragment fragment = new FavoriFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_favori, container, false);

        favoriteRecView = (RecyclerView) rootView.findViewById(R.id.favoriteRecView);
        fabAddFavorite = rootView.findViewById(R.id.fabAddFavorite);
        favorites = new ArrayList<>();

        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(getContext());

        final FavoriteAPI favoriteAPI =  retrofit.create(FavoriteAPI.class);

        Call<List<Favorite>> call = favoriteAPI.getFavorites();

        call.enqueue(new Callback<List<Favorite>>() {
            @Override
            public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                if(response.code() != 200) {
                    Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                } else {
                    List<Favorite> favoriteList = response.body();

                    for(Favorite favorite : favoriteList) {
                        favorites.add(favorite);
                    }

                    initData(favorites);
                }
            }

            @Override
            public void onFailure(Call<List<Favorite>> call, Throwable t) {

            }
        });



        fabAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddFavoriActivity.class);
                getContext().startActivity(intent);
            }
        });



        return rootView;
    }

    private void initData(List<Favorite> favorites) {
        Fragment currentFragment = getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.favoriFragment);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        FavoriteRecViewAdapter favoriteRecViewAdapter = new FavoriteRecViewAdapter(favorites,
                getContext(), transaction, currentFragment);

        favoriteRecView.setAdapter(favoriteRecViewAdapter);
        favoriteRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}