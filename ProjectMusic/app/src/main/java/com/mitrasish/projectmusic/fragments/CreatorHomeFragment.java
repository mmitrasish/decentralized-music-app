package com.mitrasish.projectmusic.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.mitrasish.projectmusic.R;

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
    private FirebaseAuth mAuth;

    private void updateUI(View root) {
        logout_btn = root.findViewById(R.id.logout_btn);
        mAuth = FirebaseAuth.getInstance();
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();


            }
        });
    }

}
