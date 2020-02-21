package ar.edu.itba.paw.webapp.auth;


import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityUserService {

    @Autowired
    UserService userService;

    public User getLoggedUser() {
        String loggedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> loggedUser = userService.findByUsername(loggedUserEmail);
        return loggedUser.orElse(null);
    }

}
