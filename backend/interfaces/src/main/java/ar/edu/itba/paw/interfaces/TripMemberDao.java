package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface TripMemberDao {

    TripMember create(Trip trip, User user, TripMemberRole role);

    Optional<TripMember> findById(long id);

    Optional<TripMember> find(Trip trip, User user);
}
