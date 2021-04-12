package com.example.civiladvocacydraft;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity  {


    //Politician object
    private Politician p;


    //Layout
    private View olayout;


    //Text and image views
    private TextView displayAddress;
    private TextView title;
    private TextView name;
    private TextView party;
    private ImageView imageView;
    private ImageView partyLogo;





    //Buttons
    private ImageButton facebookB;
    private ImageButton twitterB;
    private ImageButton youtubeB;


    //Textviews to be linkified
    private TextView phone;
    private TextView address;
    private TextView personalWebsite;



    //Since we are not returning data, everything that this activity does will be
    //handled in onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.official_activity);

        Intent intent = getIntent();

        if (intent.hasExtra("OfficialInfo")) {

            p = (Politician) intent.getSerializableExtra("OfficialInfo");
            if (p == null)
                return;




            //Initialize layout
            olayout = findViewById(R.id.officialLayout);


            //Setting the appropriate background color
            if (p.getParty().equals("Democratic Party")) {
                olayout.setBackgroundColor(getResources().getColor(R.color.dem_blue));

            }
            else if (p.getParty().equals("Republican Party")) {
                olayout.setBackgroundColor(getResources().getColor(R.color.rep_red));
            }
            else {
                olayout.setBackgroundColor(getResources().getColor(R.color.black));
            }




            //Initialize display address
            displayAddress = findViewById(R.id.officialDisplayAddress);
            displayAddress.setText(p.getDisplayAddress());

            //Initialize text and image view references
            title = findViewById(R.id.officeTitle);
            name = findViewById(R.id.politicianName);
            party = findViewById(R.id.partyLabel);
            imageView = findViewById(R.id.imageView);



            //Set textViews
            title.setText(p.getTitle());
            name.setText(p.getName());
            party.setText(p.getParty());




            //Load politician profile pictre.  If none present, use missing image.  If
            //The link does not work, use broken image.

            /*
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.placeholderimage)
                    .error(R.drawable.brokenimage)
                    .into(imageView);

             */

            loadProfilePhoto(p.getPhotoURL());





            //Initialize party logo
            partyLogo = findViewById(R.id.smallPhoto);
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





            //Initialize imageButton references
            facebookB = findViewById(R.id.facebookButton);
            twitterB = findViewById(R.id.twitterButton);
            youtubeB = findViewById(R.id.youtubeButton);


            //So for the social media buttons, only show ones where the links were parsed, and then hide
            //and disable the rest of the buttons

            if (p.getFaceBookID().equals("Not Found")) {

                //For all of these, maybe change GONE to INVISIBLE
                facebookB.setEnabled(false);
                facebookB.setVisibility(View.GONE);

            }

            if (p.getTwitterID().equals("Not Found")) {

                twitterB.setEnabled(false);
                twitterB.setVisibility(View.GONE);

            }

            if (p.getYoutubeID().equals("Not Found")) {

                youtubeB.setEnabled(false);
                youtubeB.setVisibility(View.GONE);

            }





            //Textviews to be linkified

            //Initializes phone number
            phone = findViewById(R.id.phoneNumber);
            if (!(p.getPhoneNumber().equals("not found"))) {
                phone.setText(p.getPhoneNumber());
            }



            //Initializes the address

            address = findViewById(R.id.addressLineOne);

            if (!p.getAddress().equals("Data not found")) {
                address.setText(p.getAddress());

            }




            //Sets the website URL
            personalWebsite = findViewById(R.id.politicianWebsite);
            if (!(p.getOfficialURL().equals("none"))) {
                personalWebsite.setText(p.getOfficialURL());
            }



            //If for some reason we need this next line
            //boolean result = Linkify.addLinks(phone, Linkify.ALL);
            //Note - phone just placeholder

            Linkify.addLinks(phone, Linkify.ALL);
            Linkify.addLinks(address, Linkify.ALL);
            Linkify.addLinks(personalWebsite, Linkify.ALL);



        }

        else {

            Toast.makeText(this, "Could not find politician.",Toast.LENGTH_SHORT).show();
        }


    }


    public void loadProfilePhoto(String photoUrl){
        if (doNetworkCheck()){
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
        else {
            //showMessage(ERROR_ICON, "NO NETWORK CONNECTION",
                    //"Data cannot be accessed/loaded without an Internet connection");
            imageView.setImageResource(R.drawable.brokenimage);
        }
    }



    //Network check function
    public boolean doNetworkCheck() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (cm == null) {
            return false;
        }

        if (netInfo != null ) {
            return true;
        }

        else {
            return false;
        }
    }




    //When the photo icon is clicked, launches photoactivity
    public void officialPhotoClicked(View v) {

        //Pass the photo URL as intent

        //All we need is the title, name, photo, and party logo

        //So when this is implemented, simply just pass the politician object like before


        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("OfficialPhoto", p);
        startActivity(intent);

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


    public void facebookClicked(View view){
        int currFacebookAppVersion = 3002850;
        String facebookID = p.getFaceBookID();
        String FACEBOOK_URL = "https://www.facebook.com/" + facebookID;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= currFacebookAppVersion){
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            }
            else {
                urlToUse = "fb://page/" + p.getFaceBookID();
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }


    public void twitterClicked(View view){
        Intent intent = null;
        String twitterID = p.getTwitterID();
        try {
            getPackageManager().getPackageInfo("com.twitter.android",0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterID));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }catch (Exception e){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/" + twitterID));
        }
        startActivity(intent);
    }

    public void youtubeClicked(View view){
        String youTubeChannel = p.getYoutubeID();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + youTubeChannel));
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + youTubeChannel)));
        }
    }






}
