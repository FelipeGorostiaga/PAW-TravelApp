package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserPicturesService userPicturesService;

    @Autowired
    private UserDao ud;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailingService mailService;

    @Override
    public Optional<User> findById(long id) {
        return ud.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String email) {
        return ud.findByUsername(email);
    }

    @Override
    public Optional<User> findByVerificationCode(String verificationCode) {
        return ud.findByVerificationCode(verificationCode);
    }

    @Override
    public User create(String firstname, String lastname, String email, String password, LocalDate birthday, String nationality, String sex) throws Exception {
        if (findByUsername(email).isPresent()) {
            throw new Exception();
        }
        String verificationCode;
        do {
            verificationCode = RandomStringUtils.random(64, true, true);
        }
        while (!findByVerificationCode(verificationCode).isPresent());
        User user = ud.create(firstname, lastname, email, passwordEncoder.encode(password), birthday, nationality, sex, verificationCode);
        mailService.sendRegisterMail(user);
        return user;
    }

    @Override
    public boolean update(User user) {
        return ud.update(user);
    }

    @Override
    public void verify(User user) {
        ud.verify(user);
    }

    @Override
    public List<User> findByName(String name) {
        return ud.findByName(name);
    }

    @Override
    public List<User> findInvitableUsersByName(String name, Trip trip) {
        List<User> users = findByName(name);
        List<User> tripUsers = trip.getMembers().stream().map(TripMember::getUser).collect(Collectors.toList());
        return users.stream().filter(u -> !tripUsers.contains(u) && !ud.hasTripInvitation(trip, u)).collect(Collectors.toList());
    }

    @Override
    public void changeProfilePicture(User user, byte[] imageBytes) {
        if (userPicturesService.findByUserId(user.getId()).isPresent()) {
            userPicturesService.deleteByUserId(user.getId());
        }
        userPicturesService.create(user, imageBytes);
    }

    @Override
    public void editBiography(User user, String biography) {
        ud.editBiography(user, biography);
    }


    // rates that the user received
    @Override
    public List<UserRate> getUserRates(long userId) {
        return ud.getUserRates(userId);
    }

    @Override
    public List<TripInvitation> getTripInvitations(long userId) {
        return ud.getTripInvitations(userId);
    }

    // rates that the user need to complete to other users
    @Override
    public List<UserRate> getUserPendingRates(long userId) {
        return ud.getUserPendingRates(userId);
    }

    @Override
    public double calculateRate(long userId) {
        List<UserRate> rates = getUserRates(userId);
        if (rates.isEmpty()) return 0;
        int totalRating = rates.stream().map(UserRate::getRate).reduce(0, Integer::sum);
        return totalRating / (double) rates.size();
    }
}
