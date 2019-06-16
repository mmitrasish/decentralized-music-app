package com.mitrasish.projectmusic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mitrasish.projectmusic.MediaPlayerActivity;
import com.mitrasish.projectmusic.R;
import com.mitrasish.projectmusic.models.SongCredits;
import com.mitrasish.projectmusic.models.TransactionDetails;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<TransactionDetails> transactionDetailsList;
    private Context context;

    public TransactionAdapter(List<TransactionDetails> transactionDetailsList, Context context){
        this.transactionDetailsList = transactionDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_view, parent, false);
        return new TransactionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.MyViewHolder holder, int position) {
        final TransactionDetails transactionDetails = transactionDetailsList.get(position);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DecimalFormat dcf = new DecimalFormat("0.00");

        holder.title_text.setText(transactionDetails.getTitle());
        holder.description_text.setText(transactionDetails.getDesription());
        holder.date_text.setText(df.format(new Date(transactionDetails.getDate())));
        holder.token_amount.setText(dcf.format(transactionDetails.getToken()));
        holder.type_text.setText(transactionDetails.getType());

    }

    @Override
    public int getItemCount() {
        return transactionDetailsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView title_text, description_text, date_text, token_amount, type_text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title_text = itemView.findViewById(R.id.title_text);
            description_text = itemView.findViewById(R.id.description_text);
            date_text = itemView.findViewById(R.id.date_text);
            token_amount = itemView.findViewById(R.id.token_amount);
            type_text = itemView.findViewById(R.id.type_text);
        }
    }
}
