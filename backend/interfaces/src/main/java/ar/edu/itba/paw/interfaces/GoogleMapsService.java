package ar.edu.itba.paw.interfaces;

import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

import java.util.List;

public interface GoogleMapsService {

    List<Place> queryGoogleMapsPlaces(double latitude, double longitude) throws GooglePlacesException;
    ar.edu.itba.paw.model.Place createGooglePlaceReference(List<se.walkercrou.places.Place> googlePlaces) throws GooglePlacesException;

}
