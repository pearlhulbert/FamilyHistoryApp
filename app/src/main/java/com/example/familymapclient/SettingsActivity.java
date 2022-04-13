package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Map;

import Data.DataCache;
import model.Person;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        DataCache instance = DataCache.getInstance();

        SwitchCompat maleSwitch = findViewById(R.id.maleSwitch);
        maleSwitch.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  instance.filterByGender();
                  instance.setMaleFilter(true);
              }
          }
        );
        SwitchCompat femaleSwitch = findViewById(R.id.femaleSwitch);
        femaleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instance.filterByGender();
                instance.setFemaleFilter(true);
            }
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

}