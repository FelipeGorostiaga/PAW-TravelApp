package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.Place;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@Sql("classpath:schema.sql")
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TestPlaceHibernateDao {

    private static final String googleId = "googleId";
    private static final String name = "The Moon and the starts";
    private static final String address = "Close to Earth, in the deep and vast space";
    private static final double latitude = 420;
    private static final double longitude = 69;

    @Autowired
    PlaceHibernateDao placeDao;

    @Test
    public void TestCreate() {
        Place place = placeDao.create(googleId, name, latitude, longitude, address);
        Assert.assertNotNull(place);
        Assert.assertEquals(name, place.getName());
    }

    @Test
    public void TestFindById() {
        Optional<Place> placeOptional = placeDao.findById(3);
        Assert.assertTrue(placeOptional.isPresent());
        Assert.assertEquals("Bahamas", placeOptional.get().getName());
    }

    @Test
    public void TestFindByGoogleId() {
        Optional<Place> placeOptional = placeDao.findByGoogleId("google id");
        Assert.assertTrue(placeOptional.isPresent());
        Assert.assertEquals("Bahamas", placeOptional.get().getName());
    }


}
