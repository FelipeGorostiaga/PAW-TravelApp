package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.User;

public interface TripMemberService {

    public TripMember create(Trip trip, User user, TripMemberRole role);

    public TripMember findById(long id);

    public TripMember find(Trip trip, User user);
}
