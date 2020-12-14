package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;
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
    public boolean update(User u) {
        return em.merge(u) != null;
    }

    @Override
    public void verify(User u) {
        Query query = em.createQuery("update User set verified = true where id = :userId");
        query.setParameter("userId", u.getId());
        query.executeUpdate();
    }

    @Override
    public List<User> findByName(String name) {
        final TypedQuery<User> query = em.createQuery("From User as u where u.firstname like :name or u.lastname like :name", User.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public Optional<User> findById(long id)  {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByUsername(String email) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findByVerificationCode(String verificationCode) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.verificationCode = :verificationCode", User.class);
        query.setParameter("verificationCode", verificationCode);
        return query.getResultList().stream().findFirst();
    }


}
