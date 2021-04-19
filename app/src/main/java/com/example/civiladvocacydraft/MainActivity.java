package com.example.civiladvocacydraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String tag = "MainActivity";


    private final ArrayList<Politician> politicianList = new ArrayList<>();  // Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private PoliticianAdapter pAdapter; // Data to recyclerview adapter

    private Politician p;



    //Fused Location content
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private static String locationString = "Unspecified Location";




    //Location textView
    private TextView locationDisplay;

    //Zipcde
    private String zipCode;
    private String code;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the title
        setTitle("Civil Advocacy");

        locationDisplay = findViewById(R.id.location);



        //Fall back on
        //Determine location



        recyclerView = findViewById(R.id.recycler);
        pAdapter = new PoliticianAdapter(politicianList, this);



        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        determineLocation();





        recyclerView = findViewById(R.id.recycler);
        pAdapter = new PoliticianAdapter(politicianList, this);


        recyclerView.setAdapter(pAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        locationDisplay.setText(locationString);




    }



    //Just added
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putSerializable("politicians", politicianList);
        outState.putString("location", locationDisplay.getText().toString());


        System.out.println(locationDisplay.getText().toString());


        super.onSaveInstanceState(outState);

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        locationDisplay.setText(savedInstanceState.getString("location"));

        ArrayList<Politician> nList = (ArrayList<Politician>) savedInstanceState.getSerializable("politicians");

        updateData(nList);


    }



    //Makes sure the permission is on to allow locations and then retrieves the location
    //This is responsible for displaying location in app
    private void determineLocation() {
        if (checkMyPermission()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            locationString = getPlace(location);

                            //Temporary, replace this eventually
                            Toast.makeText(MainActivity.this, locationString, Toast.LENGTH_LONG).show();
                            Log.d(tag, locationString);

                            //Sets the location title
                            //Uncomment if necessary
                            locationDisplay.setText(locationString);
                        }
                    })
                    .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }



    //The following block of methods handles the location


    //Makes sure location permission is turned on
    private boolean checkMyPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_REQUEST);
            return false;
        }
        return true;
    }


    //Properly formats the location by city, state format
    private String getPlace(Location loc) {

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String zc = addresses.get(0).getPostalCode();

            zipCode = zc;

            //Loadable called once the location is determined
            PLoaderRunnable politicianLoaderRunnable = new PLoaderRunnable(this, zipCode);
            new Thread(politicianLoaderRunnable).start();


            sb.append(String.format(
                    Locale.getDefault(),
                    "%s, %s, %s",
                    city, state, zc));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }



    //Determines whether user gave location permission or not
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    //textView.setText(R.string.deniedText);
                    //Make toast message instead
                }
            }
        }
    }



    //End of block






    //For menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        if (item.getItemId()==R.id.search_icon) {

            System.out.println("We got tot this point");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_CAP_CHARACTERS);
            et.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(et);


            builder.setPositiveButton("OK", (dialog, id) -> {

                code = et.getText().toString();
                PLoaderRunnable politicianLoaderRunnableTwo = new PLoaderRunnable(MainActivity.this, code);
                new Thread(politicianLoaderRunnableTwo).start();

            });

            builder.setNegativeButton("CANCEL", (dialog, id) -> {



            });

            builder.setMessage("Enter a state, city, or zipcode");
            builder.setTitle("Enter Address");

            AlertDialog dialog = builder.create();
            dialog.show();


        }

        else if (item.getItemId()==R.id.info_icon) {

            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);

        }



        return super.onOptionsItemSelected(item);

    }





    @Override
    public void onClick(View v) {

        int pos = recyclerView.getChildLayoutPosition(v);
        p = politicianList.get(pos);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("OfficialInfo", p);
        startActivity(intent);

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }




    public void updateData(ArrayList<Politician> pList) {
        politicianList.clear();


        politicianList.addAll(pList);

        //Just added
        locationDisplay.setText(pList.get(0).getDisplayAddress());

        pAdapter.notifyDataSetChanged();


    }

    public void downloadFailed() {
        //Toast.makeText(MainActivity.this, "Location not found: Please enter a valid city, state, and/or zipcode", Toast.LENGTH_LONG).show();
        showInvalidLocationDialog();

    }


    public void showInvalidLocationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enter a valid state, city, or zipcode");
        builder.setTitle("Invalid Location");

        AlertDialog dialog = builder.create();
        dialog.show();

    }


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



    public void networkErrorDialog( ){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Data cannot be accessed/loaded without internet connection.");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}