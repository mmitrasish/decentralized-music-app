package com.mitrasish.projectmusic.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mitrasish.projectmusic.R;
import com.mitrasish.projectmusic.adapters.ArtistListAdapter;
import com.mitrasish.projectmusic.adapters.SongAdapter;
import com.mitrasish.projectmusic.models.SongCredits;
import com.mitrasish.projectmusic.models.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListenerHomeFragment extends Fragment {


    public ListenerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_listener_home, container, false);
        updateUI(root);
        return root;
    }


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userId;
    private RecyclerView all_song_recycler, all_artist_recycler;
    private Button logout_btn;
    private List<SongCredits> allSongsList;
    private List<UserDetails> allArtistList;

    private void updateUI(View root) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        allSongsList = new ArrayList<>();
        allArtistList = new ArrayList<>();

        all_song_recycler = root.findViewById(R.id.all_song_recycler);
        all_artist_recycler = root.findViewById(R.id.all_artist_recycler);
        logout_btn = root.findViewById(R.id.logout_btn);

        all_song_recycler.setHasFixedSize(true);
        all_song_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        all_song_recycler.setAdapter(new SongAdapter(allSongsList, getContext()));

        all_artist_recycler.setHasFixedSize(true);
        all_artist_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        all_artist_recycler.setAdapter(new ArtistListAdapter(allArtistList));

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });

        loadAllMusic();
        getArtistData();
    }

    private void getArtistData() {
        DatabaseReference artistsRef = mDatabase.child("Artists");
        artistsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allArtistList.clear();
                for (DataSnapshot artistSnapshot: dataSnapshot.getChildren()) {
                    mDatabase.child("Users").child(artistSnapshot.getKey()).child("PersonalDetails").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            allArtistList.add(dataSnapshot.getValue(UserDetails.class));
                            all_artist_recycler.getAdapter().notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(getTag(), databaseError.getMessage());
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(getTag(), databaseError.getMessage());
            }
        });
    }

    private void loadAllMusic() {
        DatabaseReference songRef = mDatabase.child("Songs");
        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allSongsList.clear();
                for (DataSnapshot songSnapshot: dataSnapshot.getChildren()) {
                    SongCredits song = songSnapshot.getValue(SongCredits.class);
                    song.setSongKey(songSnapshot.getKey());
                    allSongsList.add(song);
                    all_song_recycler.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(getTag(), databaseError.getMessage());
            }
        });
    }

}
