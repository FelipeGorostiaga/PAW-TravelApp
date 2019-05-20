package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripPicturesDao;
import ar.edu.itba.paw.interfaces.TripPicturesService;
import ar.edu.itba.paw.model.TripPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TripPicturesServiceImpl implements TripPicturesService {

    @Autowired
    TripPicturesDao tpd;

    @Override
    public TripPicture create(long tripId, byte[] image) {
        return tpd.create(tripId, image);
    }

    @Override
    public Optional<TripPicture> findByTripId(long tripId) {
        return tpd.findByTripId(tripId);
    }

    @Override
    public boolean deleteByTripId(long tripId) {
        return tpd.deleteByTripId(tripId);
    }
}