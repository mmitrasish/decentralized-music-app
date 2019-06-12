package com.mitrasish.projectmusic.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mitrasish.projectmusic.CreatorBaseActivity;
import com.mitrasish.projectmusic.CreatorDetailActivity;
import com.mitrasish.projectmusic.DesignationActivity;
import com.mitrasish.projectmusic.R;

import java.util.HashMap;
import java.util.Map;
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
    private final static int REQ_CODE_PICK_SOUNDFILE = 0;
    private FirebaseStorage mStorage;
    private UploadTask uploadTask;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String userId;

    private void updateUI(View root) {
        upload_music_btn = root.findViewById(R.id.upload_music_btn);
        mStorage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();

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
                StorageReference riversRef = mStorage.getReference().child("Songs/" + uniqueSongID + ".mp3");
                uploadTask = riversRef.putFile(audioFileUri, metadata);
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
                        String key = mDatabase.child("Users").child(userId).child("Songs").push().getKey();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(key, songId);
                        mDatabase.child("Users").child(userId).child("Songs").updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                
                            }
                        });
                    }
                });
            }
        }
    }

}
