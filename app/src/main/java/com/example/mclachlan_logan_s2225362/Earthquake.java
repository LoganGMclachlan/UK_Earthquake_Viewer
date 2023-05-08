// Name                 Logan Mclachlan
// Student ID           s2225362

package com.example.mclachlan_logan_s2225362;

// class to hold info on earthquake items
public class Earthquake {

    // defines earthquake properties
    private String title;
    private String link;
    private String date;
    private double latitude;
    private double longitude;
    private double magnitude;
    private Integer depth;

    // default constructor with 0 parameters
    public Earthquake(){
        title = "";
        link = "";
        date = "";
        latitude = 0.0;
        longitude = 0.0;
        magnitude = 0.0;
    }

    // setters
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public void setDepth(Integer depth) { this.depth = depth; }

    // getters
    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public Integer getDepth() { return depth; }
}
