package com.ahmed.bank;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.bank.api.model.Favorite;
import com.ahmed.bank.api.service.FavoriteAPI;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavoriteRecViewAdapter extends RecyclerView.Adapter<FavoriteRecViewAdapter.viewHolder>{

    private List<Favorite> favorites;
    private Context mContext;
    private FragmentTransaction fragmentTransaction;
    private Fragment currentFragment;

    public FavoriteRecViewAdapter(List<Favorite> favorites, Context mContext, FragmentTransaction transaction, Fragment currentFragment) {
        this.favorites = favorites;
        this.mContext = mContext;
        this.fragmentTransaction = transaction;
        this.currentFragment = currentFragment;
    }

    public FavoriteRecViewAdapter(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_list_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.favoriteLibelle.setText(favorites.get(position).getCarte().getLibelle());
        holder.favoriteNumero.setText(String.valueOf(favorites.get(position).getCarte().getNumero()));
        holder.favoriteComments.setText(favorites.get(position).getComments());
        holder.favoriteDate.setText(favorites.get(position).getDate());

        holder.deleteFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = "are your sure ! you want to delete " +
                        favorites.get(holder.getAdapterPosition()).getCarte().getLibelle() +
                        " from favorites";

                new MaterialAlertDialogBuilder(mContext)
                        .setTitle("Delete favorite")
                        .setMessage(msg)
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("Yes delete it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(mContext);

                                final FavoriteAPI favoriteAPI = retrofit.create(FavoriteAPI.class);

                                Call<String> call = favoriteAPI.deleteFavorite(
                                        favorites.get(holder.getAdapterPosition()).getId()
                                );

                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if(response.code() == 204) {
                                            showSnackBarSuccess("Favorite was delete successfully");
                                            FavoriFragment fragment = new FavoriFragment();
                                            fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();

                                        } else{
                                            showSnackBarError("Some thing wrong please try again !");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView favoriteLibelle;
        private TextView favoriteNumero;
        private TextView favoriteComments;
        private TextView favoriteDate;

        private Button deleteFavoriteBtn;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            favoriteLibelle = itemView.findViewById(R.id.favoriteLibelle);
            favoriteNumero = itemView.findViewById(R.id.favoriteNumero);
            favoriteComments = itemView.findViewById(R.id.favoriteComments);
            favoriteDate = itemView.findViewById(R.id.favoriteDate);

            deleteFavoriteBtn = itemView.findViewById(R.id.deleteFavoriteBtn);
        }
    }

    private void showSnackBarError(String msg) {

        Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
                .setTextColor(ContextCompat.getColor(mContext,R.color.white))
                .setBackgroundTint(ContextCompat.getColor(mContext,R.color.danger))
                .show();
    }

    private void showSnackBarSuccess(String msg) {

        Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
                .show();
    }
}
