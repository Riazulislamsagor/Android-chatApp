package com.example.chattingapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chattingapp.Fragments.CallFragment;
import com.example.chattingapp.Fragments.ChatFragment;
import com.example.chattingapp.Fragments.StatusFragment;

public class FragmentAdapters extends FragmentPagerAdapter {
    public FragmentAdapters(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new ChatFragment();
            case 1:return new CallFragment();
            case 2:return new StatusFragment();
            default:return new ChatFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int position) {
        String title=null;
        if (position==0){
            title="Chats";
        }
        if (position==1){
            title="Call";
        }
        if (position==2){
            title="Status";
        }
        return title;
    }
}
