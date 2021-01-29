package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public boolean update(User u) {
        return em.merge(u) != null;
    }

    @Override
    public void verify(User u) {
        Query query = em.createQuery("UPDATE User set verified = true where id = :userId");
        query.setParameter("userId", u.getId());
        query.executeUpdate();
    }

    @Override
    public List<User> findByName(String name) {
        final TypedQuery<User> query = em.createQuery("FROM User as u where u.firstname like :name or u.lastname like :name", User.class);
        query.setParameter("name", name);
        return query.getResultList();
    }


    @Override
    public boolean editBiography(User user, String biography) {
        Query query = em.createQuery("UPDATE User set biography = :biography where id = :id");
        query.setParameter("biography", biography);
        query.setParameter("id", user.getId());
        return query.executeUpdate() != 0;
    }

    @Override
    public List<UserRate> getUserRates(long userId) {
        final TypedQuery<UserRate> query = em.createQuery("FROM UserRate as ur where ur.ratedUser.id = :userId", UserRate.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public UserRate createRate(Trip trip, User ratedBy, User ratedUser, int rate, String comment) {
        final UserRate user = new UserRate(rate,ratedUser, ratedBy, comment, LocalDateTime.now());
        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> findById(long id)  {
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
