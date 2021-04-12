package com.example.civiladvocacydraft;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;


public class PLoaderRunnable implements Runnable {


    //What to do for the PoliticianInfo Runnable:
    /*

    Store all necessary/acquired information in a Politician object
    From there, we can use that object to fill in all necessary fields
    Add this Politician object to Politician List
    Return a list of Politician objects

     */


    //Use 60610 as zip code

    //My API Key = AIzaSyAQ5Pow0tlrEs2IOIO_Vh29a6jE0jN4oTc


    //Example link
    //https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyAQ5Pow0tlrEs2IOIO_Vh29a6jE0jN4oTc&address=60610


    private final String prefixURL = "https://www.googleapis.com/civicinfo/v2/representatives";

    private final String myAPIKey = "AIzaSyAQ5Pow0tlrEs2IOIO_Vh29a6jE0jN4oTc";

    //private final String zipCode = "60610"; In this case we are actually using 60616


    private final String code;


    private static final String TAG = "PLoaderRunnable";
    private final MainActivity mainActivity;

    /*
            //For testing purposes
        //This worked, so here's the working code
        Uri.Builder buildURL = Uri.parse(prefixURL).buildUpon();
        buildURL.appendQueryParameter("key", myAPIKey);
        buildURL.appendQueryParameter("address", zipCode);
        String dummyURL = buildURL.build().toString();

        Log.d(TAG, "test: " + dummyURL);
     */


    PLoaderRunnable(MainActivity mainActivity, String zc)
    {
        this.mainActivity = mainActivity;
        this.code = zc;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        Uri.Builder buildURL = Uri.parse(prefixURL).buildUpon();
        buildURL.appendQueryParameter("key", myAPIKey);
        buildURL.appendQueryParameter("address", code);
        String urlToUse = buildURL.build().toString();

        Log.d(TAG, "test: " + urlToUse);


        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            //Showed in class for establishing a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            //Just added
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                handleResults(null);
                return;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            //Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            //System.out.println("null was returned");
            handleResults(null);
            //For debugging
            //System.out.println("Hello World");
            return;

        }

        handleResults(sb.toString());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleResults(String s) {

        if (s == null) {
            Log.d(TAG, "handleResults: Failure in data download");
            //Toast.makeText(mainActivity, "Please enter a valid city, state, and/or zipcode", Toast.LENGTH_LONG).show();
            //mainActivity.runOnUiThread(mainActivity::downloadFailed);
            mainActivity.runOnUiThread(() -> mainActivity.downloadFailed());
            return;

        }

        final ArrayList<Politician> politicianList = parseJSON(s);

        mainActivity.runOnUiThread(() -> {
            if (politicianList != null)
                Toast.makeText(mainActivity, "Loaded " + politicianList.size() + " politicians.", Toast.LENGTH_LONG).show();
            mainActivity.updateData(politicianList);
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Politician> parseJSON(String s) {

        Politician c = new Politician();
        ArrayList<Politician> politicianList = new ArrayList<>();

        try {


            //The whole API is a JSON object that contains other JSON Objects and JSON arrays
            JSONObject jObjMain = new JSONObject(s);


            try {
                JSONArray officesArray = jObjMain.getJSONArray("offices");
                JSONArray officialsArray = jObjMain.getJSONArray("officials");


                //Must loop through the offices JSON Array, which stores all the political positions/jobs held
                for (int i = 0; i < officesArray.length(); i++) {
                    JSONObject officeContent = (JSONObject) officesArray.get(i);

                    //Retrieves title/position of politician
                    String title = officeContent.getString("name");


                    StringBuilder temp = new StringBuilder();
                    JSONArray tempArr = officeContent.getJSONArray("officialIndices");
                    for (int j = 0; j < tempArr.length(); j++) {
                        temp.append(tempArr.get(j)).append(" ");
                    }


                    //This code converts a string like "1 2 3 45" to an array of ints [1,2,3,45]
                    String[] stringTokens = temp.toString().split(" ");
                    int[] officialIndices = Stream.of(stringTokens).mapToInt(strToken -> Integer.parseInt(strToken)).toArray();


                    //We know must loop through the officials JSON array to find all political officials
                    //that serve under a given position/job (in this case, "title")
                    for (int ii = 0; ii < officialIndices.length; ii++) {
                        JSONObject officialContent = (JSONObject) officialsArray.get(officialIndices[ii]);

                        //Retrieve the name of the politician
                        String name = officialContent.getString("name");


                        //For each of these, we must account for if any field we want is omitted


                        //Retrieve political party
                        String party = "";
                        if (!officialContent.has("party")) {
                            party = "Unknown";
                        } else {
                            party = officialContent.getString("party");
                        }


                        //Retrieve photoURL
                        String photoURL = "";

                        if (!officialContent.has("photoUrl")) {
                            photoURL = "none";
                        } else {
                            photoURL = officialContent.getString("photoUrl");
                        }


                        //Retrieve official URL
                        String officialURL = "";
                        if (!officialContent.has("urls")) {
                            officialURL = "none";
                        } else {
                            officialURL = officialContent.getJSONArray("urls").getString(0);
                        }


                        //Retrieve phone number
                        String phone = "";
                        if (!officialContent.has("phones")) {
                            phone = "not found";
                        } else {
                            phone = officialContent.getJSONArray("phones").getString(0);
                        }


                        //Retrieve email
                        String email = "";
                        if (!officialContent.has("emails")) {
                            email = "not found";
                        } else {
                            email = officialContent.getJSONArray("emails").getString(0);
                        }


                        //Retrieve address
                        String address = "";
                        if (!officialContent.has("address")) {
                            address = "Data not found";
                        } else {

                            //Address is a JSON Array that contains one JSON object, so we only need to loop through all lines present in that one object
                            JSONObject jsonAddressObject = officialContent.getJSONArray("address").getJSONObject(0);

                            if (jsonAddressObject.has("line1")) {
                                address += jsonAddressObject.getString("line1") + '\n';
                            }
                            if (jsonAddressObject.has("line2")) {
                                address += jsonAddressObject.getString("line2") + '\n';
                            }
                            if (jsonAddressObject.has("city")) {
                                address += jsonAddressObject.getString("city") + ", ";
                            }
                            if (jsonAddressObject.has("state")) {
                                address += jsonAddressObject.getString("state") + ' ';
                            }
                            if (jsonAddressObject.has("zip")) {
                                address += jsonAddressObject.getString("zip");
                            }
                        }


                        String twitterID = "Not Found";
                        String faceBookID = "Not Found";
                        String youtubeID = "Not Found";


                        if (officialContent.has("channels")) {

                            //Channels is an array of JSON objects
                            JSONArray channelsArray = officialContent.getJSONArray("channels");


                            //Since channels is a JSON array of JSON objects, we must loop through every JSON Object in the channels array
                            //Each time we do this, we attempt to see if that JSON object contains twitter, facebook, or youtube information
                            for (int jj = 0; jj < channelsArray.length(); jj++) {
                                JSONObject jsonChannelsObject = officialContent.getJSONArray("channels").getJSONObject(jj);

                                if (jsonChannelsObject.getString("type").equals("Twitter")) {
                                    twitterID = jsonChannelsObject.getString("id");
                                }

                                if (jsonChannelsObject.getString("type").equals("Facebook")) {
                                    faceBookID = jsonChannelsObject.getString("id");
                                }

                                if (jsonChannelsObject.getString("type").equals("YouTube")) {
                                    youtubeID = jsonChannelsObject.getString("id");
                                }
                            } //end of for loop

                        } //end of if statement for has channels


                        politicianList.add(new Politician(title, name, party, photoURL, officialURL, phone, email, address, twitterID, faceBookID, youtubeID));

                        System.out.println("Hello");


                    } //end of for loop to loop through the official indices array


                } //end of offices for loop

            } catch (Exception e) {

                e.printStackTrace();
            }

            return politicianList;
        } catch (Exception e) {

            e.printStackTrace();
        }


        return null;
    }

}
