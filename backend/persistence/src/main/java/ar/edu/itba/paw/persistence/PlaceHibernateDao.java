package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PlaceDao;
import ar.edu.itba.paw.model.Place;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class PlaceHibernateDao implements PlaceDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public Place create(String googleId, String name, double latitude, double longitude, String address) {
        Place place = new Place(googleId, name, latitude, longitude, address);
        em.persist(place);
        return place;
    }

    @Override
    public Optional<Place> findById(long id) {
        return Optional.of(em.find(Place.class, id));
    }

    @Override
    public Optional<Place> findByGoogleId(String googleId) {
        final TypedQuery<Place> query = em.createQuery("From Place as p where p.googleId like :googleId", Place.class);
        query.setParameter("googleId", googleId);
        return query.getResultList().stream().findFirst();

    }

}
