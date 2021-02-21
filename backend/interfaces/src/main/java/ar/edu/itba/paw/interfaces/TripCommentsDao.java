package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface TripCommentsDao {
    public TripComment create(Trip trip, User user, String comment);

    public Optional<TripComment> findById(long id);

    public void deleteComments(long tripId);
}
