package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chattingapp.Adapters.FragmentAdapters;
import com.example.chattingapp.databinding.ActivityMainBinding;
import com.example.chattingapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();

        //getSupportActionBar().hide();
        binding.viewpagerId.setAdapter(new FragmentAdapters(getSupportFragmentManager()));
        binding.tablayoutId.setupWithViewPager(binding.viewpagerId);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.settingId){
            Intent intent=new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (item.getItemId()==R.id.logoutId) {
            auth.signOut();
            Intent intent=new Intent(MainActivity.this,SignInActivity.class);
            startActivity(intent);

        } else if (item.getItemId()==R.id.GroupChatId) {

            Intent intent=new Intent(MainActivity.this, GroupchatActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

}