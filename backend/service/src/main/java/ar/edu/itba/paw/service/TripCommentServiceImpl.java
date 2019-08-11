package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripCommentsDao;
import ar.edu.itba.paw.interfaces.TripCommentsService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TripCommentServiceImpl implements TripCommentsService {

    @Autowired
    TripCommentsDao tripCommentsDao;

    @Override
    public TripComment create(User user, Trip trip, String comment) {

        return tripCommentsDao.create(user, trip, comment);
    }

    @Override
    public Optional<TripComment> getById(long id) {
        return tripCommentsDao.findById(id);
    }


}
