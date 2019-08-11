package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@Sql("classpath:schema.sql")
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TestUserHibernateDao {

    private static final String PASSWORD = "password2";

    private static final String LASTNAME = "lastname2";
    private static final String FIRSTNAME = "firstname2";

    private static final String EMAIL = "fgorostiaga@itba.edu.ar";
    private static final String EMAIL2 = "test@mail.com2";
    private static final String NATIONALITY = "ARG";

    private static final long ID = 1;
    private static final long ID2 = 2;

    @Autowired
    private UserHibernateDao ud;

    @Test
    public void testCreate() {
        final User user = ud.create(FIRSTNAME, LASTNAME, EMAIL2, PASSWORD, LocalDate.now(), NATIONALITY);
        Assert.assertNotNull(user);
        Assert.assertEquals(FIRSTNAME, user.getFirstname());
        Assert.assertEquals(LASTNAME, user.getLastname());
    }

    @Test
    public void TestfindById() {
        Optional<User> user = ud.findById(ID);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(ID, user.get().getId());
    }

    @Test
    public void TestfindByUsername() {
        Optional<User> user = ud.findByUsername(EMAIL);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(EMAIL, user.get().getEmail());
        Assert.assertEquals(ID, user.get().getId());
    }



}
