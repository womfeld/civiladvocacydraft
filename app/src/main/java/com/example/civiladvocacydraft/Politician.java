package com.example.civiladvocacydraft;

import java.io.Serializable;

public class Politician implements Serializable {

    private String title;
    private String name;
    private String party;
    private String photoURL;
    private String officialURL;
    private String phoneNumber;
    private String email;
    private String address;
    private String twitterID;
    private String faceBookID;
    private String youtubeID;




    
    /*
    Title
    Name
    Political Party
    PhotoURL
    OfficialURL
    PhoneNumber
    Email
    Address
    TwitterID
    FaceBookID
    youtubeID
     */


    public Politician(String title, String name, String party, String photoURL, String officialURL, String phoneNumber, String email, String address, String twitterID, String faceBookID, String youtubeID) {

        this.title = title;
        this.name = name;
        this.party = party;
        this.photoURL = photoURL;
        this.officialURL = officialURL;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.twitterID = twitterID;
        this.faceBookID = faceBookID;
        this.youtubeID = youtubeID;


    }




    public Politician() {

    }


    public String getTitle() {
        return this.title;
    }

    public String getName() {
        return this.name;
    }

    public String getParty() {
        return this.party;
    }

    public String getPhotoURL() {
        return this.photoURL;
    }

    public String getOfficialURL() {
        return this.officialURL;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAddress() {
        return this.address;
    }

    public String getTwitterID() {
        return this.twitterID;
    }

    public String getFaceBookID() {
        return this.faceBookID;
    }

    public String getYoutubeID() {
        return this.youtubeID;
    }


    //public String getURL() { return this.url; }


    public void setName(String n) {
        this.name = n;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    //public void setUrl(String u) { this.url = u; }





}
