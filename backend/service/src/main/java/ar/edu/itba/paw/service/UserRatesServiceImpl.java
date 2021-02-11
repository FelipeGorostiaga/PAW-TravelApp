package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserRatesDao;
import ar.edu.itba.paw.interfaces.UserRatesService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserRatesServiceImpl implements UserRatesService {


    @Autowired
    private UserRatesDao rateDao;

    @Override
    public UserRate createRate(Trip trip, User ratedBy, User ratedUser) {
        return rateDao.createRate(trip, ratedUser, ratedBy);
    }

    @Override
    public Optional<UserRate> findById(long rateId) {
        return rateDao.findById(rateId);
    }

    @Override
    public boolean rateUser(long rateId, int rate, String comment) {
        return rateDao.rateUser(rateId, rate, comment);
    }
}
