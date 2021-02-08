package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;

public interface UserRatesService {

    UserRate createRate(Trip trip, User ratedBy, User ratedUser);

    UserRate rateUser(Trip trip, User ratedUser, User ratedBy, int rate, String comment);
}
