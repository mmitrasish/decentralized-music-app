package com.mitrasish.projectmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mitrasish.projectmusic.adapters.SongAdapter;
import com.mitrasish.projectmusic.adapters.TransactionAdapter;
import com.mitrasish.projectmusic.models.TransactionDetails;

import java.util.ArrayList;
import java.util.List;

public class ShowTransactionActivity extends AppCompatActivity {

    private RecyclerView transaction_recycler;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private List<TransactionDetails> transactionDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transaction);

        transaction_recycler = findViewById(R.id.transaction_recycler);
        transactionDetailsList = new ArrayList<>();

        transaction_recycler.setHasFixedSize(true);
        transaction_recycler.setLayoutManager(new LinearLayoutManager(ShowTransactionActivity.this));
        transaction_recycler.setAdapter(new TransactionAdapter(transactionDetailsList, ShowTransactionActivity.this));


        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        loadTransaction(userId);

    }

    private void loadTransaction(String userId) {
        mDatabase.child("Users").child(userId).child("TokenTransaction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()){
                    transactionDetailsList.add(transactionSnapshot.getValue(TransactionDetails.class));
                    transaction_recycler.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(getLocalClassName(), databaseError.getMessage());
            }
        });

    }
}
