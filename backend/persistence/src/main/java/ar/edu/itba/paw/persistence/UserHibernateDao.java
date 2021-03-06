package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripInvitation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(String firstname, String lastname, String email, String password, LocalDate birthday, String nationality, String sex, String verificationCode) {
        final User user = new User(firstname, lastname, email, password, birthday, nationality, sex, verificationCode);
        em.persist(user);
        return user;
    }

    @Override
    public void verify(User u) {
        Query query = em.createQuery("UPDATE User SET verified = true WHERE id = :userId");
        query.setParameter("userId", u.getId());
        query.executeUpdate();
    }

    @Override
    public List<User> findByName(String name) {
        final TypedQuery<User> query = em.createQuery("FROM User AS u WHERE u.firstname LIKE :name OR u.lastname LIKE :name", User.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }


    @Override
    public boolean editBiography(User user, String biography) {
        Query query = em.createQuery("UPDATE User SET biography = :biography WHERE id = :id");
        query.setParameter("biography", biography);
        query.setParameter("id", user.getId());
        return query.executeUpdate() != 0;
    }

    @Override
    public List<UserRate> getUserRates(long userId) {
        final TypedQuery<UserRate> query = em.createQuery("FROM UserRate AS ur WHERE ur.ratedUser.id = :userId and pending = false", UserRate.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<TripInvitation> getTripInvitations(long userId) {
        final TypedQuery<TripInvitation> query = em.createQuery("FROM TripInvitation AS ti WHERE ti.invitee.id = :userId AND ti.trip.status <> 'COMPLETED'", TripInvitation.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<UserRate> getUserPendingRates(long userId) {
        final TypedQuery<UserRate> query = em.createQuery("FROM UserRate AS ur WHERE ur.ratedByUser.id = :userId AND ur.pending = true", UserRate.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public boolean hasTripInvitation(Trip trip, User u) {
        final TypedQuery<TripInvitation> query = em.createQuery("FROM TripInvitation AS ti WHERE ti.trip.id = :tripId AND ti.invitee.id = :userId AND responded = false", TripInvitation.class);
        query.setParameter("tripId", trip.getId());
        query.setParameter("userId", u.getId());
        return !query.getResultList().isEmpty();
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByUsername(String email) {
        final TypedQuery<User> query = em.createQuery("FROM User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findByVerificationCode(String verificationCode) {
        final TypedQuery<User> query = em.createQuery("FROM User as u where u.verificationCode = :verificationCode", User.class);
        query.setParameter("verificationCode", verificationCode);
        return query.getResultList().stream().findFirst();
    }

}
