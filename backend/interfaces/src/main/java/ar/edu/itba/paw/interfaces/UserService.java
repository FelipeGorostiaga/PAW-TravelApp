package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public interface UserService {

    Optional<User> findById(final long id);

    Optional<User> findByUsername(final String email);

    Optional<User> findByVerificationCode(final String verificationCode);

    User create(final String firstname, final String lastname, final String email, final String password,
                final LocalDate birthday, final String nationality, final String sex, final String verificationCOde);

    boolean update(User user);

    void verify(User user);

    List<User> findByName(final String name);

    List<User> findInvitableUsersByName(String name, Trip trip);

    void changeProfilePicture(User loggedUser, byte[] imageBytes);

    void editBiography(User loggedUser, String biography);

    List<UserRate> getUserRates(long userId);
}
