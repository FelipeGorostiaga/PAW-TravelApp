package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripMemberDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class TripMemberHibernateDao implements TripMemberDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public TripMember create(Trip trip, User user, TripMemberRole role) {
        TripMember tripMember = new TripMember(trip, user, role);
        em.persist(tripMember);
        return tripMember;
    }

    @Override
    public Optional<TripMember> findById(long id) {
        return Optional.ofNullable(em.find(TripMember.class, id));
    }

    @Override
    public Optional<TripMember> find(Trip trip, User user) {
        final TypedQuery<TripMember> query = em.createQuery("FROM TripMember as tm where tm.trip.id = :tripId AND tm.user.id = :userId", TripMember.class);
        query.setParameter("tripId", trip.getId());
        query.setParameter("userId", user.getId());
        return query.getResultList().stream().findFirst();
    }
}
