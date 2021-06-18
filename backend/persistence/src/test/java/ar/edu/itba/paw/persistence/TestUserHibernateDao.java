package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.TripInvitation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@Sql("classpath:schema.sql")
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TestUserHibernateDao {

    @Autowired
    private UserHibernateDao userDao;

    private static final String username = "feligoros@gmail.com";
    private static final String password = "password";
    private static final String firstname = "Felipe";
    private static final String lastname = "Gorostiaga";
    private static final String token = "H6YQUGLSMfDsxC6lyflIWKVlOo56r7ENy3ZOqpK8";
    private static final String nationality = "Argentina";
    private static final String sex = "M";
    private static final LocalDate birthday = LocalDate.of(1996, 6, 16);

    @Test
    public void testCreate() {
        final User user = userDao.create(firstname, lastname, username, password, birthday, nationality, sex, token);
        Assert.assertNotNull(user);
        Assert.assertEquals(username, user.getEmail());
    }

    @Test
    public void testFindById() {
        Optional<User> userOptional = userDao.findById(1);
        Assert.assertTrue(userOptional.isPresent());
        Assert.assertNotNull(userOptional.get());
    }

    @Test
    public void testFindByName() {
        List<User> users = userDao.findByName("Felipe");
        Assert.assertFalse(users.isEmpty());
        Assert.assertNotNull(users.get(0));
    }

    @Test
    public void testFindyByUsername() {
        Optional<User> userOptional = userDao.findByUsername("fgorostiaga@itba.edu.ar");
        Assert.assertTrue(userOptional.isPresent());
        Assert.assertNotNull(userOptional.get());
    }

    @Test
    public void testFindByVerificationCode() {
        Optional<User> userOptional = userDao.findByVerificationCode("secret");
        Assert.assertTrue(userOptional.isPresent());
        Assert.assertNotNull(userOptional.get());
    }

    @Test
    public void testGetUserRates() {
        List<UserRate> rates = userDao.getUserRates(1);
        Assert.assertNotNull(rates);
        Assert.assertTrue(rates.isEmpty());
    }

    @Test
    public void testGetTripInvitations() {
        List<TripInvitation> invitations = userDao.getTripInvitations(1);
        Assert.assertNotNull(invitations);
        Assert.assertTrue(invitations.isEmpty());
    }

    @Test
    public void testGetUserPendingRates() {
        List<UserRate> pendingRates = userDao.getUserPendingRates(1);
        Assert.assertNotNull(pendingRates);
        Assert.assertTrue(pendingRates.isEmpty());
    }

}
