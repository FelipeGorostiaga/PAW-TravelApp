package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@Sql("classpath:schema.sql")
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TestTripHibernateDao {

    private static final long tripId = 2;
    private static final long userId = 1;
    private static final long placeId = 3;


    private static final String COMMENT = "test comment";
    private static final String TEST_NAME = "test name";
    private static final String NAME = "Triping under the stars";
    private static final String DESC = "Lucy in the sky with diamonds";
    private static final LocalDate START_DATE = LocalDate.of(1997, 6, 16);
    private static final LocalDate END_DATE = LocalDate.of(1997, 6, 18);


    @Autowired
    private TripHibernateDao td;

    @Test
    public void testCreate() {
        Trip trip = td.create(userId, placeId, NAME, DESC, START_DATE, END_DATE);
        Assert.assertNotNull(trip);
        Assert.assertEquals(NAME, trip.getName());
        Assert.assertEquals(DESC, trip.getDescription());

    }

    @Test
    public void testFindById() {
        Optional<Trip> trip = td.findById(tripId);
        Assert.assertTrue(trip.isPresent());
        Assert.assertEquals(tripId, trip.get().getId());
    }

    @Test
    public void testFindByName() {
        List<Trip> trips = td.findByName(TEST_NAME);
        Assert.assertNotNull(trips);
        Assert.assertNotEquals(0, trips.size());
        Assert.assertEquals(tripId, trips.get(0).getId());
    }

    @Test
    public void testGetAllTrips() {
        List<Trip> trips = td.getAllTrips(1);
        Assert.assertNotNull(trips);
        Assert.assertNotEquals(0, trips.size());
        Assert.assertEquals(tripId, trips.get(0).getId());
    }

    @Test
    public void testFindUserCreatedTrips() {
        List<Trip> trips = td.findUserCreatedTrips(userId);
        Assert.assertNotNull(trips);
        Assert.assertNotEquals(0, trips.size());
        Assert.assertEquals(tripId, trips.get(0).getId());
    }

    @Test
    public void testCountAllTrips() {
        int count = td.countAllTrips();
        Assert.assertEquals(1, count );
    }

    @Test
    public void testGetTripComments() {
        List<TripComment> tripComments = td.getTripComments(tripId);
        Assert.assertNotNull(tripComments);
        Assert.assertNotEquals(0, tripComments.size());
        Assert.assertEquals(COMMENT, tripComments.get(0).getComment());
    }

}
