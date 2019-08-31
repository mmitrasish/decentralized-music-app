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
import com.mitrasish.projectmusic.models.UserDetails;

public class CreatorDetailActivity extends AppCompatActivity {

    private TextInputLayout name_input, mobile_input, address_input;
    private Button submit_btn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final String userId = mAuth.getUid();

        name_input = findViewById(R.id.name_input);
        mobile_input = findViewById(R.id.mobile_input);
        address_input = findViewById(R.id.address_input);

        submit_btn = findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String full_name = name_input.getEditText().getText().toString();
                String mobile_number = mobile_input.getEditText().getText().toString();
                String address = address_input.getEditText().getText().toString();

                if (TextUtils.isEmpty(full_name)) {
                    Toast.makeText(getApplicationContext(), "Enter full name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mobile_number)) {
                    Toast.makeText(getApplicationContext(), "Enter mobile number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getApplicationContext(), "Enter address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserDetails userDetails = new UserDetails(full_name, mobile_number, address);
                mDatabase.child("Users").child(userId).child("PersonalDetails").setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(CreatorDetailActivity.this, "Successfully Submitted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreatorDetailActivity.this, CreatorBaseActivity.class));
                            finish();
                        }else {
                            Toast.makeText(CreatorDetailActivity.this, "Not Submitted! Retry Again", Toast.LENGTH_SHORT).show();
                            name_input.getEditText().getText().clear();
                            mobile_input.getEditText().getText().clear();
                            address_input.getEditText().getText().clear();
                        }
                    }
                });
            }
        });
    }
}
