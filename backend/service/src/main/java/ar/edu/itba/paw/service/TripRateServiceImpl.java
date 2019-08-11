package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripRateDao;
import ar.edu.itba.paw.interfaces.TripRateService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripRate;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TripRateServiceImpl implements TripRateService {
    @Autowired
    TripRateDao tripRateDao;

    @Override
    public TripRate create(User user, Trip trip, int rate) {
        return tripRateDao.create(user, trip, rate);
    }

    @Override
    public Optional<TripRate> findById(long id) {
        return tripRateDao.findById(id);
    }

    @Override
    public void delete(long id) {
        tripRateDao.delete(id);
    }

    @Override
    public void update(TripRate tripRate) {
        tripRateDao.update(tripRate);
    }

    @Override
    public Optional<TripRate> findByUserAndTrip(long tripId, long userId) {
        return tripRateDao.findByUserAndTrip(tripId, userId);
    }
}
