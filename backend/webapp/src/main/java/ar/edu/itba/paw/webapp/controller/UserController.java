package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.form.EditProfileForm;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Controller
public class UserController extends MainController{

    @Autowired
    private UserPicturesService ups;

    @Autowired
    private MailingService ms;

    @Autowired
    private UserService us;

    @Autowired
    private TripService ts;

    @Autowired
    private PlaceService ps;

    @Autowired
    private TripPicturesService tripPicturesService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final long MAX_UPLOAD_SIZE = 5242880;
    private static final int MAX_TRIPS_PAGE = 6;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index"); }



    @RequestMapping("/home/{pageNum}")
    public ModelAndView home(@ModelAttribute("user") User user, @PathVariable(value = "pageNum") int pageNum) {
        ModelAndView mav = new ModelAndView("home");
        int tripQty = ts.countAllTrips();
        List<Trip> trips = ts.getAllTrips(pageNum);
        int requiredPages = (int) Math.ceil(tripQty/(double)MAX_TRIPS_PAGE);
        if(pageNum == 0 || (pageNum > 1 && pageNum > requiredPages)) {
            mav.setViewName("404");
            return mav;
        }

        List<DataPair<DataPair<Trip, Boolean>, Place>> list = new ArrayList<>();
        for(Trip trip : trips) {
            Optional<Place> startPlace = ps.findById(trip.getStartPlaceId());
            list.add(new DataPair<>(new DataPair<>(trip, tripPicturesService.findByTripId(trip.getId()).isPresent()), startPlace.get() ));
        }
        mav.addObject("pages", requiredPages);
        mav.addObject("dateFormat", formatter);
        mav.addObject("tripList", list);
        return mav;
    }

    @RequestMapping("/signup")
    public ModelAndView signup(@ModelAttribute("signupForm") final UserCreateForm form) {
        return new ModelAndView("signup");
    }

    @RequestMapping(value ="/signin", method = {RequestMethod.GET})
    public ModelAndView signin() { return new ModelAndView("signin"); }


    @RequestMapping(value = "/signup", method = {RequestMethod.POST})
    public ModelAndView validateSignUp(@Valid @ModelAttribute("signupForm") final UserCreateForm form,
                                       final BindingResult errors, HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("signup");
        if(errors.hasErrors() ) {
            return mav;
        }
        if(!form.checkPswRepeat()) {
            mav.addObject("pswRepeatError", true);
            return mav;
        }
        if(!form.checkBirthday()) {
            mav.addObject("invalidBirthday", true);
            return mav;
        }
        Optional userOpt = us.findByUsername(form.getEmail());
        if(userOpt.isPresent()) {
            mav.addObject("alreadyExists", true);
            return mav;
        }

        ms.sendRegisterMail(form.getEmail(), form.getFirstname() , form.getLastname(), getLocale());
        User user = us.create(form.getFirstname(), form.getLastname(), form.getEmail(), form.getPassword(),
                DateManipulation.stringToLocalDate(form.getBirthday()), form.getNationality());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        authToken.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mav.setViewName("redirect:/home/1");
        return mav;
    }


    @RequestMapping(value = "/home/profile/{userId}", method = {RequestMethod.GET})
    public ModelAndView profile( @PathVariable(value = "userId") long userProfileId) {
        ModelAndView mav = new ModelAndView("profile");
        Optional<User> profileUser = us.findById(userProfileId);

        if(!profileUser.isPresent()) {
            mav.setViewName("404");
            return mav;
        }
        List<Trip> userTrips = ts.getAllUserTrips(profileUser.get());
        Optional<UserPicture> userPictureOptional = ups.findByUserId(userProfileId);
        if(userPictureOptional.isPresent()) {
            mav.addObject("hasProfilePicture", true);
        }
        String birthday = profileUser.get().getBirthday().format(formatter);
        LOGGER.debug("User profile birthday {}", birthday);
        mav.addObject("trips", userTrips);
        mav.addObject("birthday", birthday);
        mav.addObject("userProfile", profileUser.get());
        return mav;
    }

    @RequestMapping(value = "/home/profile/{userId}/image", method = {RequestMethod.GET})
    public void getProfileImage(@PathVariable(value = "userId") long userProfileId, HttpServletResponse response) {
        Optional<UserPicture> userPictureOptional = ups.findByUserId(userProfileId);
        try {
            if(userPictureOptional.isPresent()) {
                UserPicture up = userPictureOptional.get();
                String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(up.getPicture()));
                response.setContentType(mimeType);
                response.getOutputStream().write(up.getPicture());
            }
        }
        catch (IOException ex) {
            //nothing to do here
        }
    }

    @RequestMapping(value = "/home/profile/{userId}/edit", method = {RequestMethod.GET})
    public ModelAndView editProfileGet(@ModelAttribute("user") User user, @PathVariable(value = "userId") long userId,
                                @ModelAttribute("editProfileForm") final EditProfileForm form) {
        ModelAndView mav = new ModelAndView("editProfile");
        if(user.getId() != userId) {
            mav.setViewName("403");
            return mav;
        }
        form.setBiography(user.getBiography());
        return mav;
    }


    @RequestMapping(value = "/home/profile/{userId}/edit", method = {RequestMethod.POST})
    public ModelAndView profileEditPost(@ModelAttribute("user") User user, @PathVariable(value = "userId") long userId,
                                @Valid @ModelAttribute("editProfileForm") final EditProfileForm form,
                                final BindingResult errors) {

        ModelAndView mav = new ModelAndView("editProfile");
        if(user.getId() != userId) {
            mav.setViewName("403");
            return mav;
        }
        if(errors.hasErrors()) {
            return mav;
        }
        Optional<User> uo = us.findById(userId);
        if(uo.isPresent()) {
            User u = uo.get();
            MultipartFile profilePicture = form.getImageUpload();
            byte[] imageBytes;
            if(profilePicture != null && !profilePicture.isEmpty()) {
                String contentType = profilePicture.getContentType();
                if(!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                    mav.addObject("invalidContentError", true);
                    return mav;
                }
                else if(profilePicture.getSize() > MAX_UPLOAD_SIZE) {
                    mav.addObject("fileSizeError", true);
                    return mav;
                }
                try {
                    imageBytes = profilePicture.getBytes();
                } catch (IOException e) {
                    mav.addObject("generalError", true);
                    return mav;
                }
                if(ups.findByUserId(userId).isPresent()) {
                    if(!ups.deleteByUserId(userId)) {
                        mav.addObject("generalError", true);
                        return mav;
                    }
                }
                UserPicture userPicture = ups.create(u, imageBytes);

            }
            u.setBiography(form.getBiography());
            us.update(u);
            String redirectFormat = String.format("redirect:/home/profile/%d", userId);
            mav.setViewName(redirectFormat);
        }
        return mav;

    }

    @RequestMapping("/about")
    public ModelAndView aboutUs() { return new ModelAndView("about"); }

}
