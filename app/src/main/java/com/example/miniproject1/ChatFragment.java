package com.example.miniproject1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.miniproject1.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_chat, container, false);
        //create a Arraylist of user model type to store info of all users
        ArrayList<UserModel> users=new ArrayList<>();



        RecyclerView recycler=v.findViewById(R.id.recycler_users);
        UserListAdapter adapter=new UserListAdapter(getContext(),users);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
// let's select all record of users im table
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                users.clear();
                for (DataSnapshot data:snapshot.getChildren())
                {
                    UserModel user=new UserModel();
                    user.uid=data.getKey();
                    user.name=data.child("name").getValue(String.class);
                    user.email=data.child("email").getValue(String.class);
                    user.password=data.child("password").getValue(String.class);
                    user.gender=data.child("gender").getValue(String.class);
                    user.pic=data.child("pic").getValue(String.class);
                    user.About=data.child("About").getValue(String.class);
                    if(!user.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                         users.add(user); //add users to the list
                    // if (user.uid!= FirebaseAuth.getInstance().getCurrentUser().getUid())

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        return v;
    }
}








