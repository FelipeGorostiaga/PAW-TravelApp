package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripInvitation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(final long id);

    Optional<User> findByUsername(final String email);

    Optional<User> findByVerificationCode(final String verificationCode);

    User create(final String firstname, final String lastname, final String email, final String password,
                LocalDate birthday, final String nationality, final String sex, final String verificationCode);

    void verify(User user);

    List<User> findByName(String name);

    boolean editBiography(User user, String biography);

    List<UserRate> getUserRates(long userId);

    List<TripInvitation> getTripInvitations(long userId);

    List<UserRate> getUserPendingRates(long userId);

    boolean hasTripInvitation(Trip trip, User user);
}
