package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
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
public class TestTripHibernateDao {

    private static final User user = new User("felipe", "gorostiaga", "test@mail.com", "secret", LocalDate.now(), "German", "F", "1231128371982" );
    private static final Place place = new Place("googleId", "Mars", 420, 69, "A couple of miles away");
    private static final String name = "My trip to Mars";
    private static final String description = "In this trip we will be going the red planet for a nice view of Earth";
    private static final LocalDate startDate = LocalDate.now();
    private static final LocalDate endDate = LocalDate.now();

    @Autowired
    TripHibernateDao tripDao;

    @Test
    public void TestCreate() {
        Trip trip = tripDao.create(user, place, name, description, startDate, endDate, false);
        Assert.assertNotNull(trip);
        Assert.assertEquals(name, trip.getName());
    }

}
