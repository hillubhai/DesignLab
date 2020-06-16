package com.techsquad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LandingActivity.class));
            return;
        }
        setContentView(R.layout.activity_dash);
        AppCompatButton start = findViewById(R.id.start);
        AppCompatButton logout = findViewById(R.id.logout);
        final RadioGroup rg = findViewById(R.id.rg);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = rg.getCheckedRadioButtonId();
                if (x == -1)
                    Toast.makeText(MainActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                else if (x == R.id.radio_needs)
                    startActivity(new Intent(MainActivity.this, NeedsActivity.class));
                else
                    startActivity(new Intent(MainActivity.this, PledgeActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(MainActivity.this).logout();
                finish();
                startActivity(new Intent(MainActivity.this, LandingActivity.class));
            }
        });
    }
}