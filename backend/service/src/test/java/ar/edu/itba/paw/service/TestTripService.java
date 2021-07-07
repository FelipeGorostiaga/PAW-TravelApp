package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.ActivityHibernateDao;
import ar.edu.itba.paw.persistence.TripCommentHibernateDao;
import ar.edu.itba.paw.persistence.TripHibernateDao;
import ar.edu.itba.paw.persistence.TripPicturesHibernateDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestTripService {

    @InjectMocks
    private TripServiceImpl tripService;

    @Mock
    private TripHibernateDao tripDao;

    @Mock
    private ActivityHibernateDao activityDao;

    @Mock
    private TripCommentHibernateDao commentsDao;

    @Mock
    private TripPicturesHibernateDao pictureDao;

    @Mock
    private UserDao userDao;

    @Mock
    private GoogleMapsServiceImpl googleMapsService;

    @Mock
    private MailingServiceImpl mailService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private TripPicturesServiceImpl tripPicturesService;

    @Mock
    private UserRatesServiceImpl userRatesService;

    private static final long TRIP_ID = 1;
    private static final int PAGE = 1;
    private static final String NAME = "Trip name";
    private static final String DESCRIPTION = "Test description";
    private static final Place PLACE = new Place("googleId", "Mars", 420, 69, "A couple of miles away");
    private static final User USER = new User("felipe", "gorostiaga", "test@mail.com", "secret", LocalDate.now(), "German", "F", "1231128371982");
    private static final double LAT = 7;
    private static final double LONG = 93;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        Trip trip = new Trip(PLACE, NAME, DESCRIPTION, LocalDate.now(), LocalDate.now(), false);
        when(tripDao.findById(TRIP_ID)).thenReturn(Optional.of(trip));
        Optional<Trip> result = tripService.findById(TRIP_ID);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isPresent());
        Assert.assertNotNull(result.get());
        Assert.assertEquals(NAME, result.get().getName());
    }

    @Test
    public void testFindByName() {
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip(PLACE, NAME, DESCRIPTION, LocalDate.now(), LocalDate.now(), false));
        when(tripDao.findByName(NAME, PAGE)).thenReturn(trips);
        List<Trip> results = tripService.findByName(NAME, PAGE);
        Assert.assertNotNull(results);
        Assert.assertFalse(results.isEmpty());
        Assert.assertEquals(NAME, results.get(0).getName());
    }

    @Test
    public void testGetAllTripsPerPage() {
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip(PLACE, NAME, DESCRIPTION, LocalDate.now(), LocalDate.now(), false));
        when(tripDao.getAllTripsPerPage(PAGE)).thenReturn(trips);
        List<Trip> results = tripService.getAllTripsPerPage(PAGE);
        Assert.assertNotNull(results);
        Assert.assertFalse(results.isEmpty());
        Assert.assertEquals(NAME, results.get(0).getName());
    }

    @Test
    public void testGetTripMembers() {
        List<TripMember> members = new ArrayList<>();
        when(tripDao.getTripMembers(TRIP_ID)).thenReturn(members);
        List<TripMember> results = tripService.getTripMembers(TRIP_ID);
        Assert.assertNotNull(results);
        Assert.assertTrue(results.isEmpty());
    }

    @Test
    public void testGetUserTrips() {
        PaginatedResult<Trip> paginatedResult = new PaginatedResult<>(new ArrayList<>(), 0);
        when(tripDao.findUserTrips(0, PAGE)).thenReturn(paginatedResult);
        PaginatedResult<Trip> result = tripService.getUserTrips(USER, PAGE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getTrips().isEmpty());
        Assert.assertEquals(0, result.getTotalTrips());
    }

    @Test
    public void testCreate() {
        Trip trip = new Trip(PLACE, NAME, DESCRIPTION, LocalDate.now(), LocalDate.now(), false);
        List<se.walkercrou.places.Place> googlePlaces = new ArrayList<>();
        when(googleMapsService.queryGoogleMapsPlaces(LAT, LONG)).thenReturn(googlePlaces);
        when(googleMapsService.createGooglePlaceReference(googlePlaces, "Place", LAT, LONG)).thenReturn(PLACE);
        when(userDao.findById(0)).thenReturn(Optional.of(USER));
        when(tripDao.create(USER, PLACE, NAME, DESCRIPTION, LocalDate.now(), LocalDate.now(), false)).thenReturn(trip);
        Trip created = tripService.create(0, LAT, LONG, NAME, DESCRIPTION, LocalDate.now(), LocalDate.now(), false, "googlePlace", "Place");
        Assert.assertNotNull(created);
        Assert.assertEquals(NAME, created.getName());
    }

    @Test
    public void testDeleteTrip() {
        Trip trip = new Trip(PLACE, NAME, DESCRIPTION, LocalDate.now(), LocalDate.now(), false);
        tripService.deleteTrip(trip);
        verify(tripDao).deleteTrip(0);
        verify(tripDao).deleteTripInvitations(0);
        verify(mailService).sendDeleteTripMail(trip);
        verify(tripDao).deleteAllTripMembers(0);
        verify(activityDao).deleteActivities(0);
    }

}
