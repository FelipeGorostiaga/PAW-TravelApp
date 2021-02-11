package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;

import java.util.Optional;

public interface UserRatesDao {

    UserRate createRate(Trip trip, User ratedBy, User ratedUser);

    boolean rateUser(long rateId, int rate, String comment);

    Optional<UserRate> findById(long rateId);

    Optional<UserRate> findUserRate(Trip trip, User ratedUser, User ratedBy);
}
