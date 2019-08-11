package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserPicturesDao;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class UserPicturesHibernateDao implements UserPicturesDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public UserPicture create(User user, byte[] image) {
        UserPicture up = new UserPicture(image, user);
        em.persist(up);
        return up;
    }

    @Override
    public Optional<UserPicture> findByUserId(long userId) {
        final TypedQuery<UserPicture> query = em.createQuery("From UserPicture as up where up.user.id = :userId", UserPicture.class);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public boolean deleteByUserId(long userId) {
        final Query upDeletion = em.createQuery("delete UserPicture as up where up.user.id = :userId");
        upDeletion.setParameter("userId", userId);
        return upDeletion.executeUpdate() == 1;
    }
}
