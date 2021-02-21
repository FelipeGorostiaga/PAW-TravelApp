package ar.edu.itba.paw.interfaces;

import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

import java.util.List;

public interface GoogleMapsService {

    public List<Place> queryGoogleMapsPlaces(double latitude, double longitude) throws GooglePlacesException;

    public ar.edu.itba.paw.model.Place createGooglePlaceReference(List<se.walkercrou.places.Place> googlePlaces, String placeInput,
                                                           double latitude, double longitude) throws GooglePlacesException;

    public Place findGooglePlaceById(String googlePlaceId);

}
