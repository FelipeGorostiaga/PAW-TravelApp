package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Place;

public class PlaceDTO {

    private long id;
    private String googleId;
    private String name;
    private double latitude;
    private double longitude;
    private String address;

    public PlaceDTO() {
        // Empty constructor needed by JAX-RS
    }

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.googleId = place.getGoogleId();
        this.name = place.getName();
        this.address = place.getAddress();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
