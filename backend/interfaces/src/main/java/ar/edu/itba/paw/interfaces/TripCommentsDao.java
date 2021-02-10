package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.User;
import java.util.Optional;

public interface TripCommentsDao {
    TripComment create(Trip trip, User user, String comment);
    Optional<TripComment> findById(long id);
    void deleteComments(long tripId);
}
