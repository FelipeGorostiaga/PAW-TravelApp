package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(final long id);

    Optional<User> findByUsername(final String email);

    Optional<User> findByVerificationCode(final String verificationCode);

    User create(final String firstname, final String lastname, final String email, final String password,
                LocalDate birthday, final String nationality, final String sex, final String verificationCode);

    boolean update(User u);

    void verify(User u);

    List<User> findByName(String name);

    boolean editBiography(User user, String biography);
}
