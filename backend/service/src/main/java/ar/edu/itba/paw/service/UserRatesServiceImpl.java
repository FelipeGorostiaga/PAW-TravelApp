package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserRatesService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRatesServiceImpl implements UserRatesService {

    @Autowired
    UserDao userDao;

    @Override
    public UserRate createRate(Trip trip, User ratedBy, User ratedUser) {
        return userDao.createRate(trip, ratedUser, ratedBy);
    }

    @Override
    public UserRate rateUser(Trip trip, User ratedUser, User ratedBy, int rate, String comment) {
        if (userDao.rateUser(trip, ratedUser, ratedBy, rate, comment)) {
            Optional<UserRate> rateOptional = userDao.findUserRate(trip, ratedUser, ratedBy);
            return rateOptional.orElse(null);
        }
        return null;
    }
}
