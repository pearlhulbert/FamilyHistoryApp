package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


import android.os.Bundle;


public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Iconify.with(new FontAwesomeModule());
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main);
        if (fragment == null) {
            fragment = createLoginFragment();
            fragmentManager.beginTransaction().add(R.id.main, fragment).commit();
        }
       else {
           if (fragment instanceof LoginFragment) {
               ((LoginFragment) fragment).registerListener(this);
           }
        }
    }


    private Fragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        fragment.registerListener(this);
        return fragment;
    }

    @Override
    public void notifyDone() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();
        fragmentManager.beginTransaction().replace(R.id.main, fragment).commit();
    }
}