package com.mitrasish.projectmusic.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mitrasish.projectmusic.MusicCreditsActivity;
import com.mitrasish.projectmusic.R;
import com.mitrasish.projectmusic.adapters.SongAdapter;
import com.mitrasish.projectmusic.models.SongCredits;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatorDashboardFragment extends Fragment {


    public CreatorDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_creator_dashboard, container, false);
        updateUI(root);
        return root;
    }

    private Button upload_music_btn;
    private RecyclerView songs_recyclerview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private final static int REQ_CODE_PICK_SOUNDFILE = 0;
    private FirebaseStorage mStorage;
    private UploadTask uploadTask;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userId;
    private List<SongCredits> songCredits;

    private void updateUI(View root) {
        upload_music_btn = root.findViewById(R.id.upload_music_btn);
        songs_recyclerview = root.findViewById(R.id.songs_recyclerview);
        mStorage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        songCredits = new ArrayList<>();
        songs_recyclerview.setHasFixedSize(true);
        songs_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        songs_recyclerview.setAdapter(new SongAdapter(songCredits, getContext()));

        loadCreatorMusic();

        upload_music_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(Intent.createChooser(intent, "Select Your Music"), REQ_CODE_PICK_SOUNDFILE);
            }
        });
    }

    private void loadCreatorMusic() {
        DatabaseReference songRef = mDatabase.child("Users").child(userId).child("Songs");
        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                songCredits.clear();
                for (DataSnapshot songSnapshot: dataSnapshot.getChildren()) {
                    mDatabase.child("Songs").child(songSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            SongCredits song = dataSnapshot.getValue(SongCredits.class);
                            song.setSongKey(dataSnapshot.getKey());
                            songCredits.add(song);
                            songs_recyclerview.getAdapter().notifyDataSetChanged();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){
                Uri audioFileUri = data.getData();
                // Now you can use that Uri to get the file path, or upload it, ...
                final String uniqueSongID = UUID.randomUUID().toString();
                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("audio/mp3")
                        .build();
                StorageReference songRef = mStorage.getReference().child("Songs/" + uniqueSongID + ".mp3");
                uploadTask = songRef.putFile(audioFileUri, metadata);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getActivity(), "Upload Not Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...

                        String songId = uniqueSongID + ".mp3";
                        Toast.makeText(getActivity(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MusicCreditsActivity.class);
                        intent.putExtra("song_source_id", songId);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        }
    }

}
