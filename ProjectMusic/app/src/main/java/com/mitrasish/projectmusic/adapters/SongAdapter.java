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

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

    private List<SongCredits> songCreditsList;
    private Context context;

    public SongAdapter(List<SongCredits> songCreditsList, Context context){
        this.songCreditsList = songCreditsList;
        this.context = context;
    }

    @NonNull
    @Override
    public SongAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.creator_song_list_view, parent, false);
        return new SongAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.MyViewHolder holder, int position) {
        final SongCredits songCredits = songCreditsList.get(position);

        holder.song_name.setText(songCredits.getSongName());
        holder.produced_by.setText(songCredits.getProducedBy());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MediaPlayerActivity.class);
                intent.putExtra("Song_Key", songCredits.getSongKey());
                context.startActivity(intent);
            }
        });

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
