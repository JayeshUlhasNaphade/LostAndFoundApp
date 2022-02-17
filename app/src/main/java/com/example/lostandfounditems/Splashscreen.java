package com.example.lostandfounditems;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Splashscreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;

                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    i = new Intent(Splashscreen.this, MainActivity2.class);
                }
                else {

                    i = new Intent(Splashscreen.this,MainActivity.class);

                }
                startActivity(i);
                finish();
            }
        },2000);
    }
}