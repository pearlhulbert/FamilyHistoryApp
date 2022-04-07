package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Iconify.with(new FontAwesomeModule());
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();
        fragmentManager.beginTransaction().add(R.id.main, fragment).commit();
    }
}