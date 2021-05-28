package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.TripInvitation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface UserService {

    Optional<User> findById(final long id);

    Optional<User> findByUsername(final String email);

    Optional<User> findByVerificationCode(final String verificationCode);

    User create(final String firstname, final String lastname, final String email, final String password,
                final LocalDate birthday, final String nationality, final String sex) throws Exception;

    void verify(User user);

    List<User> findByName(final String name);

    List<User> findInvitableUsersByName(String name, long tripId);

    void changeProfilePicture(User loggedUser, byte[] imageBytes);

    void editBiography(User loggedUser, String biography);

    List<UserRate> getUserRates(long userId);

    List<TripInvitation> getTripInvitations(long userId);

    List<UserRate> getUserPendingRates(long userId);

    double calculateRate(long userId);
}
