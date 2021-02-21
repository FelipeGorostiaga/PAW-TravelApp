package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripMemberDao;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TripMemberServiceImpl implements TripMemberService {

    @Autowired
    private TripMemberDao tripMemberDao;

    @Override
    public TripMember create(Trip trip, User user, TripMemberRole role) {
        return null;
    }

    @Override
    public TripMember findById(long id) {
        return tripMemberDao.findById(id).orElse(null);
    }

    @Override
    public TripMember find(Trip trip, User user) {
        return tripMemberDao.find(trip, user).orElse(null);
    }
}
