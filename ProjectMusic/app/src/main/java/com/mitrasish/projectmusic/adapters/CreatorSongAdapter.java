package com.mitrasish.projectmusic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mitrasish.projectmusic.R;
import com.mitrasish.projectmusic.models.SongCredits;

import java.util.List;

public class CreatorSongAdapter extends RecyclerView.Adapter<CreatorSongAdapter.MyViewHolder> {

    private List<SongCredits> songCreditsList;

    public CreatorSongAdapter(List<SongCredits> songCreditsList){
        this.songCreditsList = songCreditsList;
    }

    @NonNull
    @Override
    public CreatorSongAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.creator_song_list_view, parent, false);
        return new CreatorSongAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatorSongAdapter.MyViewHolder holder, int position) {
        SongCredits songCredits = songCreditsList.get(position);

        holder.song_name.setText(songCredits.getSongName());
        holder.produced_by.setText(songCredits.getProducedBy());

    }

    @Override
    public int getItemCount() {
        return songCreditsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView song_name, produced_by;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            song_name = itemView.findViewById(R.id.song_name);
            produced_by = itemView.findViewById(R.id.produced_by);
        }
    }
}
