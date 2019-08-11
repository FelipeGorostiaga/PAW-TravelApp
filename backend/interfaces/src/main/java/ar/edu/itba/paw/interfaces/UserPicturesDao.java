package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;

import java.util.Optional;

public interface UserPicturesDao {

    public UserPicture create(User user, byte[] image);

    public Optional<UserPicture> findByUserId(long userId);

    public boolean deleteByUserId(long userId);

}
