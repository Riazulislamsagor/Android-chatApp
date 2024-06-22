package com.example.chattingapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chattingapp.Models.Users;
import com.example.chattingapp.UserAdapter;
import com.example.chattingapp.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatFragment extends Fragment {

    public ChatFragment(){

    }
    FragmentChatBinding binding;
    ArrayList<Users> usersArrayList=new ArrayList<>();
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding=FragmentChatBinding .inflate(inflater, container, false);

        database=FirebaseDatabase.getInstance();
        UserAdapter adapter=new UserAdapter(usersArrayList,getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.chatrecycleId.setLayoutManager(layoutManager);
        binding.chatrecycleId.setAdapter(adapter);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users=dataSnapshot.getValue(Users.class);
                  users.setUserId(dataSnapshot.getKey());
                  if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                      usersArrayList.add(users);
                  }

                }
                adapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return binding.getRoot();
    }
}