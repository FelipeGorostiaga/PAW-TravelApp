package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserRatesDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class UserRatesHibernateDao implements UserRatesDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean rateUser(long rateId, int rate, String comment) {
        Query query = em.createQuery("UPDATE UserRate SET rate = :rate, comment = :comment, createdOn = :createdOn, pending = false WHERE id = :rateId");
        query.setParameter("rate", rate);
        query.setParameter("comment", comment);
        query.setParameter("rateId", rateId);
        query.setParameter("createdOn", LocalDateTime.now());
        return query.executeUpdate() != 0;
    }

    @Override
    public Optional<UserRate> findById(long rateId) {
        return Optional.ofNullable(em.find(UserRate.class, rateId));
    }


    @Override
    public UserRate createRate(Trip trip, User ratedUser, User ratedBy) {
        final UserRate rate = new UserRate(trip, ratedUser, ratedBy);
        em.persist(rate);
        return rate;
    }
}
