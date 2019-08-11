package ar.edu.itba.paw.interfaces;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface TripCommentsService {
    TripComment create(User user, Trip trip, String comment);
    Optional<TripComment> getById(long id);

}
