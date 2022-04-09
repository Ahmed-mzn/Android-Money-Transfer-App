package com.ahmed.bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.bank.api.model.Transaction;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class TransactionRecViewAdapter extends RecyclerView.Adapter<TransactionRecViewAdapter.ViewHolder> {

    private List<Transaction> transactions = new ArrayList<>();
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public TransactionRecViewAdapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.mContext = context;

        sharedPreferences = context.getSharedPreferences("bank_app", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int cartId = sharedPreferences.getInt("carte_id", 0);

        holder.fromCarte.setText(transactions.get(position).getFromCarte().getLibelle());
        if(transactions.get(position).getToCarte() != null) {
            holder.toCarte.setText(transactions.get(position).getToCarte().getLibelle());
        } else {
            holder.toCarte.setText(" ");
        }

        switch (transactions.get(position).getType()) {
            case "transfer":
                if(transactions.get(position).getFromCarte().getId() == cartId){
                    holder.fromCarte.setText(transactions.get(position).getToCarte().getLibelle());
                    holder.toCarte.setText("Vous avez envoyer de l'argent");
                    holder.transactionAmount.setChipBackgroundColor(mContext.getColorStateList(R.color.danger));
                    holder.transactionAmount.setChipIcon(mContext.getDrawable(R.drawable.ic_chip_icon_minus));
                } else {
                    holder.fromCarte.setText(transactions.get(position).getFromCarte().getLibelle());
                    holder.toCarte.setText("a vous envoyer de l'argent");
                }
                break;
            case "retrait":
                holder.fromCarte.setText("Retrait");
                holder.toCarte.setText("un retrait est faite depuis votre compte");
                holder.transactionType.setVisibility(View.GONE);
                holder.transactionAmount.setChipBackgroundColor(mContext.getColorStateList(R.color.danger));
                holder.transactionAmount.setChipIcon(mContext.getDrawable(R.drawable.ic_chip_icon_minus));
                break;
            case "versement":
                holder.fromCarte.setText("Versement");
                holder.toCarte.setText("un versement est faite depuis " +
                        transactions.get(position).getFromCarte().getLibelle());
                holder.transactionType.setVisibility(View.GONE);
            default:
                break;
        }
        holder.transactionType.setText(transactions.get(position).getType());
        holder.transactionDate.setText(transactions.get(position).getDate());
        holder.transactionAmount.setText(String.valueOf(transactions.get(position).getAmount())+" $");
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView fromCarte;
        private TextView toCarte;
        private TextView transactionType;
        private TextView transactionDate;
        private Chip transactionAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fromCarte = itemView.findViewById(R.id.fromCarte);
            toCarte = itemView.findViewById(R.id.toCarte);
            transactionType = itemView.findViewById(R.id.transactionType);
            transactionDate = itemView.findViewById(R.id.transactionDate);
            transactionAmount = itemView.findViewById(R.id.transactionAmount);
        }
    }
}
