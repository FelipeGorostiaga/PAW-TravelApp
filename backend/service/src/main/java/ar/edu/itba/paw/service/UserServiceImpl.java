package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private TripService tripService;

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
        while (findByVerificationCode(verificationCode).isPresent());
        User user = ud.create(firstname, lastname, email, passwordEncoder.encode(password), birthday, nationality, sex, verificationCode);
        mailService.sendRegisterMail(user);
        return user;
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
    public List<User> findInvitableUsersByName(String name, long tripId) {
        List<User> users = findByName(name);
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (tripOptional.isPresent()) {
            List<User> tripUsers = tripOptional.get().getMembers().stream().map(TripMember::getUser).collect(Collectors.toList());
            return users.stream().filter(u -> !tripUsers.contains(u) && !ud.hasTripInvitation(tripOptional.get(), u)).collect(Collectors.toList());
        }
        return new ArrayList<>();
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

    @Override
    public List<UserRate> getUserRates(long userId) {
        return ud.getUserRates(userId);
    }

    @Override
    public List<TripInvitation> getTripInvitations(long userId) {
        return ud.getTripInvitations(userId);
    }

    @Override
    public List<UserRate> getUserPendingRates(long userId) {
        return ud.getUserPendingRates(userId);
    }

}
