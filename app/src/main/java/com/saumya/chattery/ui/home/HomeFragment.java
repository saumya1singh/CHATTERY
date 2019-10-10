package com.saumya.chattery.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.saumya.chattery.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    SharedPreferences sharedPreferences;
    String name , phone;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("Name","");
        phone = sharedPreferences.getString("Phone","");

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText( " Welcome " + name + " to Chattery ❤");
            }
        });

        return root;
    }
}