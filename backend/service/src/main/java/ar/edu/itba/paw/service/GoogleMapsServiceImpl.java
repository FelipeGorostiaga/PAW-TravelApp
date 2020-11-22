package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.GoogleMapsService;
import ar.edu.itba.paw.interfaces.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GoogleMapsServiceImpl implements GoogleMapsService {

    @Autowired
    GooglePlaces googleClient;

    @Autowired
    PlaceService placeService;

    @Override
    public List<Place> queryGoogleMapsPlaces(double latitude, double longitude) throws GooglePlacesException {
        return googleClient.getNearbyPlacesRankedByDistance(latitude, longitude);
    }

    @Override
    public ar.edu.itba.paw.model.Place createGooglePlaceReference(List<Place> googlePlaces) throws GooglePlacesException {
        if(googlePlaces != null && !googlePlaces.isEmpty()) {
            se.walkercrou.places.Place googlePlace = googlePlaces.get(0);
            Optional<ar.edu.itba.paw.model.Place> maybePlace = placeService.findByGoogleId(googlePlace.getPlaceId());
            return maybePlace.orElseGet(() -> placeService
                    .create(googlePlace.getPlaceId(), googlePlace.getName(), googlePlace.getLatitude(),
                            googlePlace.getLongitude(), googlePlace.getAddress()));
        }
        throw new GooglePlacesException("500", "Error creating place reference");
    }
}
