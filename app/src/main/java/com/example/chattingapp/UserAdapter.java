package com.example.chattingapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.Models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    ArrayList<Users> usersArrayList;
    Context context;

    public UserAdapter(ArrayList<Users> usersArrayList, Context context) {
        this.usersArrayList = usersArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users=usersArrayList.get(position);
        Picasso.with(context).load(users.getProfilepic()).placeholder(R.drawable.man)
                .into(holder.imageView);
        holder.username.setText(users.getUsername());

        FirebaseDatabase.getInstance().getReference("chats")
                        .child(FirebaseAuth.getInstance().getUid()+users.getUserId())
                                .orderByChild("timestamps")
                                        .limitToLast(1)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.hasChildren()){
                                                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                                holder.lastmessage.setText(dataSnapshot.child("message").getValue().toString());
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatdetailActivity.class);
                intent.putExtra("usersId",users.getUserId());
                intent.putExtra("profilepic",users.getProfilepic());
                intent.putExtra("username",users.getUsername());
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView username,lastmessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.profile_image);
            username=itemView.findViewById(R.id.usernameId);
            lastmessage=itemView.findViewById(R.id.lastmessageId);
        }
    }
}
