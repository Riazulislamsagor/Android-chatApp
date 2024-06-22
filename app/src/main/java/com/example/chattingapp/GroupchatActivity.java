package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.chattingapp.Adapters.ChatAdapter;
import com.example.chattingapp.Models.MessageModel;
import com.example.chattingapp.databinding.ActivityGroupchatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class GroupchatActivity extends AppCompatActivity {

    ActivityGroupchatBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupchatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModels=new ArrayList<>();
        final ChatAdapter adapter=new ChatAdapter(messageModels,this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.groupchatRecycleId.setLayoutManager(layoutManager);
        binding.groupchatRecycleId.setAdapter(adapter);
        final String senderId= FirebaseAuth.getInstance().getUid();

        binding.groupchatdetailtextView.setText("Friends Group");
        binding.groupchatdetailback.setOnClickListener(v -> onBackPressed());
        String senderid=FirebaseAuth.getInstance().getUid();

        database.getReference().child("GroupChat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessageModel model = dataSnapshot.getValue(MessageModel.class);
                    messageModels.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.groupsendid.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final String message=binding.groupchatedit.getText().toString();
            final MessageModel model=new MessageModel(senderid,message);
            model.setTimestamp(new Date().getTime());
            binding.groupchatedit.setText("");

            database.getReference().child("GroupChat").push().setValue(model)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

        }
        });


    }
}