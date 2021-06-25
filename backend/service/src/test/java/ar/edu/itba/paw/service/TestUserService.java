package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.TripInvitation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRate;
import ar.edu.itba.paw.persistence.TripHibernateDao;
import ar.edu.itba.paw.persistence.UserHibernateDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TestUserService {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private TripServiceImpl tripService;

    @Mock
    private UserHibernateDao userDao;

    @Mock
    private TripHibernateDao tripDao;

    private static final User USER = new User("felipe", "gorostiaga", "test@mail.com", "secret", LocalDate.now(), "German", "F", "1231128371982");
    private static final String VERIFICATION_CODE = "1231128371982";
    private static final String FIRSTNAME = "Test";
    private static final long TRIP_ID = 1;
    private static final long USER_ID = 1;


    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        when(userDao.findById(1)).thenReturn(Optional.of(USER));
        Optional<User> userOptional = userService.findById(1);
        assertNotNull(userOptional);
        assertTrue(userOptional.isPresent());
        assertNotNull(userOptional.get());
        assertEquals("felipe", userOptional.get().getFirstname());
    }

    @Test
    public void testFindByName() {
        List<User> users = new ArrayList<>();
        users.add(USER);
        when(userDao.findByName("felipe")).thenReturn(users);
        List<User> results = userService.findByName("felipe");
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals("felipe", results.get(0).getFirstname());
    }

    @Test
    public void testFindByVerificationCode() {
        when(userDao.findByVerificationCode(VERIFICATION_CODE)).thenReturn(Optional.of(USER));
        Optional<User> userOptional = userService.findByVerificationCode(VERIFICATION_CODE);
        assertNotNull(userOptional);
        assertTrue(userOptional.isPresent());
        assertNotNull(userOptional.get());
        assertEquals(VERIFICATION_CODE, userOptional.get().getVerificationCode());
    }


    @Test
    public void testFindInvitableUsersByName() {
        List<User> users = new ArrayList<>();
        when(userService.findByName(FIRSTNAME)).thenReturn(users);
        when(tripService.findById(USER_ID)).thenReturn(Optional.empty());
        when(tripDao.findById(TRIP_ID)).thenReturn(Optional.empty());
        List<User> results = userService.findInvitableUsersByName(FIRSTNAME, TRIP_ID);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetUserRates() {
        List<UserRate> rates = new ArrayList<>();
        when(userDao.getUserRates(USER_ID)).thenReturn(rates);
        List<UserRate> results = userService.getUserRates(USER_ID);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetTripInvitations() {
        List<TripInvitation> invitations = new ArrayList<>();
        when(userDao.getTripInvitations(USER_ID)).thenReturn(invitations);
        List<TripInvitation> results = userService.getTripInvitations(USER_ID);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetUserPendingRates() {
        List<UserRate> pendingRates = new ArrayList<>();
        when(userDao.getUserPendingRates(USER_ID)).thenReturn(pendingRates);
        List<UserRate> results = userService.getUserPendingRates(USER_ID);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

}
