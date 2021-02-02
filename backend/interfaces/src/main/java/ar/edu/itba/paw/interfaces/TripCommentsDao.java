package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.User;
import java.util.Optional;

public interface TripCommentsDao {
    TripComment create(TripMember member, Trip trip, String comment);
    Optional<TripComment> findById(long id);
    void deleteComments(long tripId);
}
