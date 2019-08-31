package com.mitrasish.projectmusic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mitrasish.projectmusic.R;
import com.mitrasish.projectmusic.models.SongCredits;
import com.mitrasish.projectmusic.models.UserDetails;

import java.util.HashMap;
import java.util.List;

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.MyViewHolder> {

    private List<UserDetails> userDetailsList;

    public ArtistListAdapter(List<UserDetails> userDetailsList){
        this.userDetailsList = userDetailsList;
    }

    @NonNull
    @Override
    public ArtistListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_artist_item_view, parent, false);
        return new ArtistListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistListAdapter.MyViewHolder holder, int position) {
        UserDetails userDetails = userDetailsList.get(position);

        holder.artist_name_text.setText(userDetails.getFullName());


    }

    @Override
    public int getItemCount() {
        return userDetailsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView artist_name_text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            artist_name_text = itemView.findViewById(R.id.artist_name_text);
        }
    }
}
