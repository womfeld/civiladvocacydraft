package com.example.civiladvocacydraft;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        //Set title
        ActionBar actionBar = getSupportActionBar();

        setTitle("Civil Advocacy");



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        finish();
        super.onBackPressed();
    }


    public void onTextClicked(View v) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse("https://developers.google.com/civic-information/"));
        startActivity(browserIntent);
    }

}