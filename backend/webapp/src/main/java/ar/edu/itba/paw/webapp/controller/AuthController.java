package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.auth.JwtUtil;
import ar.edu.itba.paw.webapp.auth.SecurityUserService;
import ar.edu.itba.paw.webapp.auth.TravelUserDetailsService;
import ar.edu.itba.paw.webapp.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;


@Path("authenticate")
@RestController
@Produces(value = {MediaType.APPLICATION_JSON})
public class AuthController {


    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private static final int JWT_ACCESS_EXPIRATION = 3600 * 4 * 1000; //4 hours
    private static final int JWT_REFRESH_EXPIRATION = 900000 * 1000; //10+ days

    @Autowired
    private TravelUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Valid AuthenticationRequestDTO authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(new ErrorDTO("Invalid username or password", "credentials")).build();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
        if (!userOptional.isPresent() || !userOptional.get().isVerified()) {
            return Response.status(Response.Status.FORBIDDEN).entity(new ErrorDTO("Please verify your email", "verification")).build();
        }
        final UserDTO user = new UserDTO(userOptional.get());
        final String accessToken = jwtUtil.generateToken(userDetails, JWT_ACCESS_EXPIRATION);
        final String refreshToken = jwtUtil.generateToken(userDetails, JWT_REFRESH_EXPIRATION);
        return Response.ok(new AuthenticationResponseDTO(accessToken, refreshToken, user)).build();
    }

    @GET
    @Path("/refresh")
    public Response refreshJwtToken(@HeaderParam("x-refresh-token") String refreshToken) {
        if (refreshToken != null) {
            try {
                String username = jwtUtil.extractUsername(refreshToken);
                if (username != null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(refreshToken, userDetails)) {
                        final String newAccessToken = jwtUtil.generateToken(userDetails, JWT_ACCESS_EXPIRATION);
                        return Response.ok(new RefreshResponseDTO(newAccessToken)).build();
                    }
                }
            } catch (Exception ignored) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/verify")
    public Response verifyUserRegistration(@QueryParam("code") final String verificationCode) {
        Optional<User> userOptional = userService.findByVerificationCode(verificationCode);
        if (!userOptional.isPresent() || userOptional.get().isVerified()) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorDTO("Invalid user verification code or user is already verified", "verification")).build();
        }
        User user = userOptional.get();
        userService.verify(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        final String accessToken = jwtUtil.generateToken(userDetails, JWT_ACCESS_EXPIRATION);
        final String refreshToken = jwtUtil.generateToken(userDetails, JWT_REFRESH_EXPIRATION);
        return Response.ok(new AuthenticationResponseDTO(accessToken, refreshToken, new UserDTO(user))).build();
    }
}
