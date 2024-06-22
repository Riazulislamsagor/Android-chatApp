package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chattingapp.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");

        binding.signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.logineditTextTextEmailAddress2.getText().toString().isEmpty()){
                    binding.logineditTextTextEmailAddress2.setError("Enter your email");
                    return;
                }
                if (binding.logineditTextTextPassword.getText().toString().isEmpty()){
                    binding.logineditTextTextPassword.setError("Enter your password");
                    return;
                }
                progressDialog.show();

                auth.signInWithEmailAndPassword(binding.logineditTextTextEmailAddress2.getText().toString(),
                        binding.logineditTextTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            Intent intent=new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });


        binding.loginsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        if (auth.getCurrentUser()!=null){
            Intent intent=new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }

}