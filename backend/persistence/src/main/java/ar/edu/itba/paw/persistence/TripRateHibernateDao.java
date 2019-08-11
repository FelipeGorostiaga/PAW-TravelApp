package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripRateDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripRate;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class TripRateHibernateDao implements TripRateDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public TripRate create(User user, Trip trip, int rate) {
        TripRate tripRate = new TripRate(rate, LocalDateTime.now(),trip, user   );
        em.persist(tripRate);
        return tripRate;
    }

    @Override
    public Optional<TripRate> findById(long id)  {
        return Optional.of(em.find(TripRate.class, id));
    }

    @Override
    public void delete(long id) {
        Query deleteQuery = em.createQuery("delete TripRate as tripRate where tripRate.id = :id");
        deleteQuery.setParameter("id", id);
        deleteQuery.executeUpdate();
    }

    @Override
    public void update(TripRate tripRate) {
        em.merge(tripRate);
    }

    @Override
    public Optional<TripRate> findByUserAndTrip(long tripId, long userId) {
        final TypedQuery<TripRate> query = em.createQuery("From TripRate as tr where tr.trip.id = :tripId and tr.user.id = :userId", TripRate.class);
        query.setParameter("tripId", tripId);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findAny();
    }
}
