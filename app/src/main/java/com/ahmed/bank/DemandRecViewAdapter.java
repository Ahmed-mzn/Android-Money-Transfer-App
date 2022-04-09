package com.ahmed.bank;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.bank.api.model.DataTransferValidate;
import com.ahmed.bank.api.model.Demand;
import com.ahmed.bank.api.model.Transaction;
import com.ahmed.bank.api.service.DemandAPI;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DemandRecViewAdapter extends RecyclerView.Adapter<DemandRecViewAdapter.viewHolder> {

    private List<Demand> demands = new ArrayList<>();
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private FragmentTransaction fragmentTransaction;

    public DemandRecViewAdapter(List<Demand> demands, Context context, FragmentTransaction fragmentTransaction) {
        this.demands = demands;
        this.mContext = context;
        this.fragmentTransaction = fragmentTransaction;

        sharedPreferences = context.getSharedPreferences("bank_app", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.demands_list_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.demandFrom.setText(demands.get(position).getFromCarte().getLibelle());
        holder.demandTo.setText(demands.get(position).getToCarte().getLibelle());
        holder.demandDate.setText(demands.get(position).getDate());
        holder.demandStatus.setText(demands.get(position).getStatus());
        holder.demandComments.setText(demands.get(position).getComments());
        holder.demandAmount.setText(String.valueOf(demands.get(position).getAmount()));

        int cartId = sharedPreferences.getInt("carte_id", 0);


        if(cartId == demands.get(holder.getAdapterPosition()).getFromCarte().getId()) {
            holder.refuseDemandBtn.setVisibility(View.GONE);
            holder.acceptDemandBtn.setVisibility(View.GONE);
            holder.demandFrom.setText(demands.get(position).getToCarte().getLibelle());
            holder.demandTo.setText("Vous avez lui envoyer demand d'argent");
            holder.demandAmount.setChipIcon(mContext.getDrawable(R.drawable.ic_baseline_call_made_24));
        } else {
            holder.demandFrom.setText(demands.get(position).getFromCarte().getLibelle());
            holder.demandTo.setText("Vous demand d'argent");
            holder.demandAmount.setChipIcon(mContext.getDrawable(R.drawable.ic_baseline_call_received_24));
        }

        if(demands.get(holder.getAdapterPosition()).getStatus().equals("accepted") |
                demands.get(holder.getAdapterPosition()).getStatus().equals("refused")) {
            if(demands.get(holder.getAdapterPosition()).getStatus().equals("refused")) {
                holder.demandStatus.setTextColor(ContextCompat.getColor(mContext,R.color.danger));
            } else if(demands.get(holder.getAdapterPosition()).getStatus().equals("accepted")) {
                holder.demandStatus.setTextColor(ContextCompat.getColor(mContext,R.color.success));
            }
            holder.refuseDemandBtn.setVisibility(View.GONE);
            holder.acceptDemandBtn.setVisibility(View.GONE);
        }

        holder.refuseDemandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "are your sure ! you want to refuse demand from " +
                        demands.get(holder.getAdapterPosition()).getFromCarte().getLibelle() +" for " +
                        demands.get(holder.getAdapterPosition()).getAmount();

                new MaterialAlertDialogBuilder(mContext)
                        .setTitle("Refuse demand")
                        .setMessage(msg)
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes refuse it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(mContext);

                                final DemandAPI demandAPI = retrofit.create(DemandAPI.class);

                                Call<String> call = demandAPI.refuseDemand(
                                        demands.get(holder.getAdapterPosition()).getId()
                                );

                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if(response.code() == 200) {
                                            showSnackBarSuccess("Demande refusee avec success");

                                            DemandFragment fragment = new DemandFragment();
                                            fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();

                                        } else {
                                            showSnackBarError("Inforamtions incorrect ressayer");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        showSnackBarError( "Something wrong please try again");
                                        showSnackBarError("Something wrong please try again");
                                    }
                                });
                            }
                        })
                        .show();
            }
        });

        holder.acceptDemandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "are your sure ! if you accept the demand from " +
                        demands.get(holder.getAdapterPosition()).getFromCarte().getLibelle() +", " +
                        demands.get(holder.getAdapterPosition()).getAmount() + " well be send to him";

                new MaterialAlertDialogBuilder(mContext)
                        .setTitle("Accept demand")
                        .setMessage(msg)
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("Yes accept it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataTransferValidate dataTransferValidate = new DataTransferValidate(
                                  demands.get(holder.getAdapterPosition()).getFromCarte().getNumero(),
                                  demands.get(holder.getAdapterPosition()).getAmount()
                                );
                                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(mContext);

                                final DemandAPI demandAPI = retrofit.create(DemandAPI.class);

                                Call<Transaction> call = demandAPI.acceptDemand(
                                        demands.get(holder.getAdapterPosition()).getId(),
                                        dataTransferValidate
                                );

                                call.enqueue(new Callback<Transaction>() {
                                    @Override
                                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                                        if(response.code() == 201) {
                                            showSnackBarSuccess("Demande accepte avec success");
                                            DemandFragment fragment = new DemandFragment();
                                            fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();

                                        } else {
                                            showSnackBarError("Inforamtions incorrect ressayer");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Transaction> call, Throwable t) {
                                        showSnackBarError( "Something wrong please try again");
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
        return demands.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView demandFrom;
        private TextView demandTo;
        private TextView demandDate;
        private TextView demandStatus;
        private TextView demandComments;

        private Chip demandAmount;

        private Button acceptDemandBtn;
        private Button refuseDemandBtn;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            demandFrom = itemView.findViewById(R.id.demandFrom);
            demandTo = itemView.findViewById(R.id.demandTo);
            demandAmount = itemView.findViewById(R.id.demandAmount);
            demandDate = itemView.findViewById(R.id.demandDate);
            demandStatus = itemView.findViewById(R.id.demandStatus);
            demandComments = itemView.findViewById(R.id.demandComments);

            acceptDemandBtn = itemView.findViewById(R.id.acceptDemandBtn);
            refuseDemandBtn = itemView.findViewById(R.id.refuseDemandBtn);
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
