package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceListDTO {

    List<PlaceDTO> places;

    public PlaceListDTO() {

    }

    public PlaceListDTO(List<Place> list) {
        this.places = new ArrayList<>();
        list.forEach(p -> this.places.add(new PlaceDTO(p)));
    }

    public List<PlaceDTO> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceDTO> places) {
        this.places = places;
    }
}
