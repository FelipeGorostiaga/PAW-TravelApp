package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserRatesService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRatesServiceImpl implements UserRatesService {

    @Autowired
    UserDao userDao;

    @Override
    public UserRate createRate(Trip trip, User ratedBy, User ratedUser, int rate, String comment) {
        return userDao.createRate(trip, ratedBy, ratedUser, rate, comment);
    }
}
