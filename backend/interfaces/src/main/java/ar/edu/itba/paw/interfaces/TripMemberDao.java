package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface TripMemberDao {

    public TripMember create(Trip trip, User user, TripMemberRole role);

    public Optional<TripMember> findById(long id);

    public Optional<TripMember> find(Trip trip, User user);
}
