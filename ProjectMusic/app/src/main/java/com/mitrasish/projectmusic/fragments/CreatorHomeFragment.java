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
import com.mitrasish.projectmusic.models.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatorHomeFragment extends Fragment {


    public CreatorHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_creator_home, container, false);
        updateUI(root);
        return root;
    }

    private Button logout_btn;
    private RecyclerView all_artist_recyclerview;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private List<UserDetails> userDetailsList;

    private void updateUI(View root) {
        logout_btn = root.findViewById(R.id.logout_btn);
        all_artist_recyclerview = root.findViewById(R.id.all_artist_recyclerview);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userDetailsList = new ArrayList<>();

        all_artist_recyclerview.setHasFixedSize(true);
        all_artist_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        all_artist_recyclerview.setAdapter(new ArtistListAdapter(userDetailsList));

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });

        getArtistData();
    }

    private void getArtistData() {
        DatabaseReference artistsRef = mDatabase.child("Artists");
        artistsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDetailsList.clear();
                for (DataSnapshot artistSnapshot: dataSnapshot.getChildren()) {
                    mDatabase.child("Users").child(artistSnapshot.getKey()).child("PersonalDetails").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userDetailsList.add(dataSnapshot.getValue(UserDetails.class));
                            all_artist_recyclerview.getAdapter().notifyDataSetChanged();
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

}
