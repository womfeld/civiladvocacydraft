package com.example.civiladvocacydraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    private Politician p;

    private View plyt;

    private TextView title;
    private TextView name;
    private ImageView imageView;
    private ImageView partyLogo;

    private TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);

        //Set the title
        setTitle("Civil Advocacy");


        Intent intent = getIntent();

        if (intent.hasExtra("OfficialPhoto")) {

            p = (Politician) intent.getSerializableExtra("OfficialPhoto");
            if (p == null)
                return;



            //Initialize layout
            plyt = findViewById(R.id.photoLayout);


            //Initialize the title

            textView = findViewById(R.id.textView);
            textView.setText(p.getDisplayAddress());


            //Setting the appropriate background color
            if (p.getParty().equals("Democratic Party")) {
                plyt.setBackgroundColor(getResources().getColor(R.color.dem_blue));

            }
            else if (p.getParty().equals("Republican Party")) {
                plyt.setBackgroundColor(getResources().getColor(R.color.rep_red));
            }
            else {
                plyt.setBackgroundColor(getResources().getColor(R.color.black));
            }



            //Initialize text and image view references
            title = findViewById(R.id.officeTitle2);
            name = findViewById(R.id.politicianName2);
            imageView = findViewById(R.id.largePhoto);
            partyLogo = findViewById(R.id.smallLogo2);



            //Set textViews
            title.setText(p.getTitle());
            name.setText(p.getName());


            //Load image for Photo Activity
            loadLargeProfilePhoto(p.getPhotoURL());



            //Initialize party logo
            if (p.getParty().equals("Democratic Party")) {

                partyLogo.setImageResource(R.drawable.dem_logo);

            }

            else if (p.getParty().equals("Republican Party")) {
                partyLogo.setImageResource(R.drawable.rep_logo);

            }

            else {
                partyLogo.setEnabled(false);
                partyLogo.setVisibility(View.GONE);
            }


        }

        else {

            Toast.makeText(this, "Could not find politician.",Toast.LENGTH_SHORT).show();
        }

    }


    public void loadLargeProfilePhoto(String photoUrl){

        imageView.setImageResource(R.drawable.placeholder);
        if (p.getPhotoURL().equals("none")) {
            imageView.setImageResource(R.drawable.missing);
        }
        else {
            final String url = photoUrl;
            Picasso picasso = new Picasso.Builder(this).listener((picasso1, uri, exception) -> {
                // Here we try https if the http image attempt failed
                final String secureUrl = url.replace("http:", "https:");
                picasso1.get().load(secureUrl)
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);
            }).build();

            picasso.get().load(photoUrl)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }

    }



    //When political party logo clicked
    public void partyLogoClicked(View v) {

        Intent i = new Intent(Intent.ACTION_VIEW);

        //Change this so that it is either democrat or republican link
        String partyLink;

        if (p.getParty().equals("Democratic Party")) {

            partyLink = "https://democrats.org/";
            i.setData(Uri.parse(partyLink));
            startActivity(i);

        }

        if (p.getParty().equals("Republican Party")) {

            partyLink = "https://www.gop.com/";
            i.setData(Uri.parse(partyLink));
            startActivity(i);

        }

    }






}