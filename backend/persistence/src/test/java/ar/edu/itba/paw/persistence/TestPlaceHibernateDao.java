package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.PlaceDao;
import ar.edu.itba.paw.model.Place;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@Sql("classpath:schema.sql")
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TestPlaceHibernateDao {


    private static final long placeId = 3;
    private static final String GOOGLE_ID = "google id";
    private static final String NAME = "Buenos Aires, Argentina";
    private static final double LAT = 110;
    private static final double LONG = -150;
    private static final String ADDRESS = "Av. Juan B Justo 1045";

    @Autowired
    private PlaceDao pd;

    @Test
    public void testCreate() {
        Place place = pd.create(GOOGLE_ID, NAME, LAT, LONG, ADDRESS);
        Assert.assertNotNull(place);
        Assert.assertEquals(NAME, place.getName());
        Assert.assertEquals(ADDRESS, place.getAddress());
    }

    @Test
    public void testFindById() {
        Optional<Place> placeOptional = pd.findById(placeId);
        Assert.assertTrue(placeOptional.isPresent());
        Assert.assertEquals(placeId, placeOptional.get().getId());
    }

    @Test
    public void testFindByGoogleId() {
        Optional<Place> placeOptional = pd.findByGoogleId(GOOGLE_ID);
        Assert.assertTrue(placeOptional.isPresent());
        Assert.assertEquals(placeId, placeOptional.get().getId());
    }


}
