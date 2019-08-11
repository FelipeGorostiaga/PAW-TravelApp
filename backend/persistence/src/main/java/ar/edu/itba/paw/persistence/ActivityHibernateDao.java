package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ActivityHibernateDao implements ActivityDao {

    private static final int MAX_ROWS = 5;

    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<Activity> findById(long id) {
        return Optional.of(em.find(Activity.class, id));
    }

    @Override
    public Optional<Activity> findByName(String name) {
        final TypedQuery<Activity> query = em.createQuery("From Activity as a where a.name like :name", Activity.class);
        query.setParameter("name", name);
        query.setMaxResults(MAX_ROWS);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Activity create(String name, String category, Place place, Trip trip, LocalDate startDate, LocalDate endDate) {
        Activity activity = new Activity(name, category, place, trip, startDate, endDate);
        em.persist(activity);
        return activity;
    }

    @Override
    public Optional<Activity> findByCategory(String category) {
        final TypedQuery<Activity> query = em.createQuery("From Activity as a where a.category like :category", Activity.class);
        query.setParameter("category", category);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void deleteActivities(long tripId) {
        Query activityDelete = em.createQuery("delete Activity as a where a.trip.id = :tripId");
        activityDelete.setParameter("tripId", tripId);
        activityDelete.executeUpdate();
    }

    @Override
    public void deleteActivity(long activityId) {
        Query activityDelete = em.createQuery("delete Activity as a where a.id = :activityId");
        activityDelete.setParameter("activityId", activityId);
        activityDelete.executeUpdate();
    }
    @Override
    public List<Activity> getTripActivities(long tripId) {
        final TypedQuery<Activity> query = em.createQuery("From Activity as a where a.trip.id = :tripId", Activity.class);
        query.setParameter("tripId", tripId);
        return query.getResultList();
    }

}
