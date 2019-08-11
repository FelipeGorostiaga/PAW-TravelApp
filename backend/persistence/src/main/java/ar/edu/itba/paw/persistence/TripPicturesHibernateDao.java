package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripPicturesDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripPicture;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class TripPicturesHibernateDao implements TripPicturesDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public TripPicture create(Trip trip, byte[] image) {
        TripPicture tp = new TripPicture(image, trip);
        em.persist(tp);
        return tp;
    }

    @Override
    public Optional<TripPicture> findByTripId(long tripId) {
        final TypedQuery<TripPicture> query = em.createQuery("From TripPicture as tp where tp.trip.id = :tripId", TripPicture.class);
        query.setParameter("tripId", tripId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public boolean deleteByTripId(long tripId) {
        final Query tpDeletion = em.createQuery("delete TripPicture as tp where tp.trip.id = :tripId");
        tpDeletion.setParameter("tripId", tripId);
        return tpDeletion.executeUpdate() == 1;
    }
}
