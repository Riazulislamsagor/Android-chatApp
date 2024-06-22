package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chattingapp.Adapters.ChatAdapter;
import com.example.chattingapp.Models.MessageModel;
import com.example.chattingapp.databinding.ActivityChatdetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatdetailActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;

    ActivityChatdetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getSupportActionBar().hide();


        final String senderId = auth.getUid();
        String reciveId = getIntent().getStringExtra("usersId");
        String username = getIntent().getStringExtra("username");
        String profilepic = getIntent().getStringExtra("profilepic");
        binding.chatdetailtextView.setText(username);
        Picasso.with(getApplication()).load(profilepic).placeholder(R.drawable.man).into(binding.chatdetailprofile);

        binding.chatdetailback.setOnClickListener(v -> onBackPressed());

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter adapter = new ChatAdapter(messageModels, this, reciveId);
        binding.chatdetailRecycleId.setHasFixedSize(true);
        binding.chatdetailRecycleId.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatdetailRecycleId.setLayoutManager(layoutManager);

        final String senderRoom = senderId + reciveId;
        final String receiverRoom = reciveId + senderId;


        database.getReference().child("chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            MessageModel model=dataSnapshot.getValue(MessageModel.class);
                            model.setMessageId(dataSnapshot.getKey());
                            messageModels.add(model);

                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.sendid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = binding.chatdetailedit.getText().toString();
                MessageModel model = new MessageModel(senderId, message);
                model.setTimestamp(new Date().getTime());
                binding.chatdetailedit.setText("");


                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(model)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }

                        });

            }
        });


    }
}


