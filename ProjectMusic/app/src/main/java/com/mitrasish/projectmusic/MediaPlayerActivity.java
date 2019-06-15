package com.mitrasish.projectmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
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
import com.google.firebase.storage.StorageReference;
import com.mitrasish.projectmusic.helpers.MediaPlayerHolder;
import com.mitrasish.projectmusic.helpers.PlaybackInfoListener;
import com.mitrasish.projectmusic.interfaces.PlayerAdapter;
import com.mitrasish.projectmusic.models.SongCredits;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MediaPlayerActivity extends AppCompatActivity {

    private TextView song_name_text, composed_by_text;
    private ImageView play_btn, play_next_btn, play_previous_btn, like_btn, dislike_btn;
    private SeekBar songSeekBar;
    private String song_key;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userId;
    private StorageReference mStorage;
    private PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;
    final String today_date;
    private SongCredits current_song;

    public MediaPlayerActivity() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        today_date = df.format(c);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        if (getIntent() != null){
            song_key = getIntent().getStringExtra("Song_Key");
        }

        songSeekBar = findViewById(R.id.songSeekBar);
        song_name_text = findViewById(R.id.song_name_text);
        composed_by_text = findViewById(R.id.composed_by_text);
        play_btn = findViewById(R.id.play_btn);
        play_next_btn = findViewById(R.id.play_next_btn);
        play_previous_btn = findViewById(R.id.play_previous_btn);
        like_btn = findViewById(R.id.like_btn);
        dislike_btn = findViewById(R.id.dislike_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayerAdapter.isPlaying()){
                    play_btn.setImageResource(R.drawable.play);
                    mPlayerAdapter.pause();
                }else {
                    play_btn.setImageResource(R.drawable.pause);
                    mPlayerAdapter.play();
                }
            }
        });


        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("Contribution").child(today_date).child(userId).child("SongDisliked").child(song_key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            Toast.makeText(getApplicationContext(), "Does not have dislike", Toast.LENGTH_SHORT).show();
                            mDatabase.child("Contribution").child(today_date).child(userId).child("SongLiked").child(song_key).setValue(1);
                            if (!userId.equalsIgnoreCase(current_song.getSongOwner())){
                                mDatabase.child("Contribution").child(today_date).child(current_song.getSongOwner()).child("SongLiker").child(song_key).child(userId).setValue(1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                like_btn.setEnabled(false);
                dislike_btn.setEnabled(false);
            }
        });

        dislike_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("Contribution").child(today_date).child(userId).child("SongLiked").child(song_key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            Toast.makeText(getApplicationContext(), "Does not have like", Toast.LENGTH_SHORT).show();
                            mDatabase.child("Contribution").child(today_date).child(userId).child("SongDisliked").child(song_key).setValue(1);
                            if (!userId.equalsIgnoreCase(current_song.getSongOwner())){
                                mDatabase.child("Contribution").child(today_date).child(current_song.getSongOwner()).child("SongDisLiker").child(song_key).child(userId).setValue(1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                like_btn.setEnabled(false);
                dislike_btn.setEnabled(false);
            }
        });

        loadSong();
        initializePlaybackController();
        initializeSeekbar();


    }

    private void initializePlaybackController() {
        MediaPlayerHolder mMediaPlayerHolder = new MediaPlayerHolder(this);
        mMediaPlayerHolder.setPlaybackInfoListener(new PlaybackListener());
        mPlayerAdapter = mMediaPlayerHolder;
    }

    private void initializeSeekbar() {
        songSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = false;
                        mPlayerAdapter.seekTo(userSelectedPosition);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isChangingConfigurations() && mPlayerAdapter.isPlaying()) {
        } else {
            mPlayerAdapter.release();
        }
    }

    private void loadSong() {
        mDatabase.child("Songs").child(song_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current_song = dataSnapshot.getValue(SongCredits.class);
                song_name_text.setText(current_song.getSongName());
                composed_by_text.setText(current_song.getComposedBy());
                mStorage.child("Songs/" + current_song.getSongSource()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mPlayerAdapter.loadMedia(uri);
                        mPlayerAdapter.play();
                        play_btn.setImageResource(R.drawable.pause);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onDurationChanged(int duration) {
            songSeekBar.setMax(duration);
        }

        @Override
        public void onPositionChanged(int position) {
            if (!mUserIsSeeking) {
                songSeekBar.setProgress(position, true);

            }
        }

        @Override
        public void onStateChanged(@State int state) {
            String stateToString = PlaybackInfoListener.convertStateToString(state);
            onLogUpdated(String.format("onStateChanged(%s)", stateToString));
        }

        @Override
        public void onPlaybackCompleted() {
            play_btn.setImageResource(R.drawable.play);

            mDatabase.child("Contribution").child(today_date).child(userId).child("SongListened").child(song_key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Integer numOfSongPlayed = dataSnapshot.getValue(Integer.class);
                        mDatabase.child("Contribution").child(today_date).child(userId).child("SongListened").child(song_key).setValue(numOfSongPlayed + 1);
                    }else {
                        mDatabase.child("Contribution").child(today_date).child(userId).child("SongListened").child(song_key).setValue(1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if (!userId.equalsIgnoreCase(current_song.getSongOwner())){
                mDatabase.child("Contribution").child(today_date).child(current_song.getSongOwner()).child("SongListener").child(song_key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Integer numOfSongPlayed = dataSnapshot.getValue(Integer.class);
                            mDatabase.child("Contribution").child(today_date).child(current_song.getSongOwner()).child("SongListener").child(song_key).setValue(numOfSongPlayed + 1);
                        }else {
                            mDatabase.child("Contribution").child(today_date).child(current_song.getSongOwner()).child("SongListener").child(song_key).setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        @Override
        public void onLogUpdated(String message) {

        }
    }
}
