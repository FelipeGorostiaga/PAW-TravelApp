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

@RunWith(SpringJUnit4ClassRunner.class)
@Sql("classpath:schema.sql")
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TestUserHibernateDao {

    private static final String username = "feligoros@gmail.com";
    private static final String password = "password";
    private static final String firstname = "Felipe";
    private static final String lastname = "Gorostiaga";
    private static final String token = "H6YQUGLSMfDsxC6lyflIWKVlOo56r7ENy3ZOqpK8";
    private static final String nationality = "Argentina";
    private static final String sex = "M";
    private static final LocalDate birthday = LocalDate.of(1996, 6, 16);


    @Autowired
    private UserHibernateDao userDao;

    @Test
    public void testCreate() {
        final User user = userDao.create(firstname, lastname, username, password, birthday, nationality, sex, token);
        Assert.assertNotNull(user);
        Assert.assertEquals(username, user.getEmail());
    }

}
