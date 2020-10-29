package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.User;


import java.time.LocalDate;
import java.util.Optional;


public interface UserService {

    Optional<User> findById(final long id);
    Optional<User> findByUsername(final String email);
    User create(final String firstname, final String lastname, final String email, final String password,
                final LocalDate birthday, final String nationality, final String sex);
    boolean update(User user);

}
