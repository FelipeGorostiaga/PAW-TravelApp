package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.*;
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

    private static final User user = new User("felipe", "gorostiaga", "test@mail.com", "secret", LocalDate.now(), "German", "F", "1231128371982");
    private static final Place place = new Place("googleId", "Mars", 420, 69, "A couple of miles away");
    private static final String name = "My trip to Mars";
    private static final String description = "In this trip we will be going the red planet for a nice view of Earth";
    private static final LocalDate startDate = LocalDate.now();
    private static final LocalDate endDate = LocalDate.now();
    private static final String TOKEN = "token";

    @Autowired
    private TripHibernateDao tripDao;

    @Test
    public void testCreate() {
        Trip trip = tripDao.create(user, place, name, description, startDate, endDate, false);
        Assert.assertNotNull(trip);
        Assert.assertEquals(name, trip.getName());
    }

    @Test
    public void testFindById() {
        Optional<Trip> tripOptional = tripDao.findById(1);
        Assert.assertTrue(tripOptional.isPresent());
        Assert.assertNotNull(tripOptional.get());
    }

    @Test
    public void testFindByName() {
        List<Trip> trips = tripDao.findByName("Test Trip", 1);
        Assert.assertNotNull(trips);
        Assert.assertNotNull(trips.get(0));
        Assert.assertEquals("Test Trip", trips.get(0).getName());
    }

    @Test
    public void testFindJoinRequestByToken() {
        Optional<TripPendingConfirmation> pendingConfirmationOptional = tripDao.findJoinRequestByToken(TOKEN);
        Assert.assertTrue(pendingConfirmationOptional.isPresent());
        Assert.assertNotNull(pendingConfirmationOptional.get());
    }

    @Test
    public void testGetAllTrips() {
        List<Trip> trips = tripDao.getAllTripsPerPage(1);
        Assert.assertNotNull(trips);
        Assert.assertFalse(!trips.isEmpty());
    }

    @Test
    public void testFindTripInvitationByUser() {
        Trip trip = new Trip(place, "Trip name", "Trip description", startDate, endDate, false);
        Optional<TripInvitation> optionalTripInvitation = tripDao.findTripInvitationByUser(trip, user);
        Assert.assertFalse(optionalTripInvitation.isPresent());
    }

    @Test
    public void testUpdateTripData() {
        tripDao.updateTripData("New name", "New description", 1);
        Trip trip = tripDao.findById(1).get();
        Assert.assertEquals("New name", trip.getName());
    }

    @Test
    public void testMarkTripAsCompleted() {
        tripDao.markTripAsCompleted(1);
        Optional<Trip> tripOptional = tripDao.findById(1);
        Assert.assertEquals(TripStatus.COMPLETED, tripOptional.get().getStatus());
    }

    @Test
    public void testGetTripJoinRequests() {
        List<TripPendingConfirmation> joinRequests = tripDao.getTripJoinRequests(1);
        Assert.assertNotNull(joinRequests);
        Assert.assertFalse(joinRequests.isEmpty());
    }

    @Test
    public void testGetTripMembers() {
        List<TripMember> members = tripDao.getTripMembers(1);
        Assert.assertNotNull(members);
        Assert.assertFalse(members.isEmpty());
    }

    @Test
    public void testGetTripComments() {
        List<TripComment> comments = tripDao.getTripComments(1);
        Assert.assertNotNull(comments);
        Assert.assertFalse(comments.isEmpty());
    }

}
