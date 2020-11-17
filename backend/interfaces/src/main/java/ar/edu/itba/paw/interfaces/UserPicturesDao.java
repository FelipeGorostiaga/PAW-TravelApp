package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;

import java.util.Optional;

public interface UserPicturesDao {

    UserPicture create(User user, byte[] image);
    Optional<UserPicture> findByUserId(long userId);
    boolean deleteByUserId(long userId);

}
