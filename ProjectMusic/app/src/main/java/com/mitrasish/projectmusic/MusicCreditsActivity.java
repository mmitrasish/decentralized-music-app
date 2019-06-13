package com.mitrasish.projectmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mitrasish.projectmusic.models.SongCredits;

import java.util.HashMap;
import java.util.Map;

public class MusicCreditsActivity extends AppCompatActivity {

    private String music_key_id, music_source_id;
    private TextInputLayout song_name_input, composed_by_input, lyrics_by_input, music_by_input, directed_by_input, produced_by_input;
    private Button submit_song_btn;
    private String userId;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_credits);

        if (getIntent() != null){
            music_source_id = getIntent().getStringExtra("song_source_id");
        }
        song_name_input = findViewById(R.id.song_name_input);
        composed_by_input = findViewById(R.id.composed_by_input);
        lyrics_by_input = findViewById(R.id.lyrics_by_input);
        music_by_input = findViewById(R.id.music_by_input);
        directed_by_input = findViewById(R.id.directed_by_input);
        produced_by_input = findViewById(R.id.produced_by_input);
        submit_song_btn = findViewById(R.id.submit_song_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();

        submit_song_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String song_name = song_name_input.getEditText().getText().toString();
                String composed_by = composed_by_input.getEditText().getText().toString();
                String lyrics_by = lyrics_by_input.getEditText().getText().toString();
                String music_by = music_by_input.getEditText().getText().toString();
                String directed_by = directed_by_input.getEditText().getText().toString();
                String produced_by = produced_by_input.getEditText().getText().toString();

                if (TextUtils.isEmpty(song_name)) {
                    song_name_input.setError("Enter Song Name!");
                    return;
                }
                if (TextUtils.isEmpty(composed_by)) {
                    composed_by_input.setError("Enter Composed By!");
                    return;
                }
                if (TextUtils.isEmpty(lyrics_by)) {
                    lyrics_by_input.setError("Enter Lyrics By!");
                    return;
                }
                if (TextUtils.isEmpty(music_by)) {
                    music_by_input.setError("Enter Music By!");
                    return;
                }
                if (TextUtils.isEmpty(directed_by)) {
                    directed_by_input.setError("Enter Directed By!");
                    return;
                }
                if (TextUtils.isEmpty(produced_by)) {
                    produced_by_input.setError("Enter Produced By!");
                    return;
                }


                music_key_id = mDatabase.child("Songs").push().getKey();
                SongCredits songCredits = new SongCredits(song_name, composed_by, lyrics_by, music_by, directed_by, produced_by, music_source_id);
                Map<String, Object> songValue = songCredits.toMap();
                final Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(music_key_id, songValue);
                mDatabase.child("Songs").updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mDatabase.child("Users").child(userId).child("Songs").child(music_key_id).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MusicCreditsActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MusicCreditsActivity.this, CreatorBaseActivity.class));
                                finish();
                            }
                        });
                    }
                });

            }
        });

    }
}
