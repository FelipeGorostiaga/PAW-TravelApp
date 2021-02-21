package ar.edu.itba.paw.interfaces;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface TripCommentsService {
    public TripComment create(User user, Trip trip, String comment);
    public Optional<TripComment> getById(long id);

}
