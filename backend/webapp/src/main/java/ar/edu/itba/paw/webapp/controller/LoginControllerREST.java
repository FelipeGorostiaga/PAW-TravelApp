package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.auth.TravelUserDetailsService;
import ar.edu.itba.paw.webapp.auth.util.JwtUtil;
import ar.edu.itba.paw.webapp.dto.AuthenticationRequestDTO;
import ar.edu.itba.paw.webapp.dto.AuthenticationResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class LoginControllerREST {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TravelUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @POST
    @Path("/login")
    public Response createAuthenticationToken(@RequestParam @Valid AuthenticationRequestDTO authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        }
        catch (BadCredentialsException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return Response.ok(new AuthenticationResponseDTO(jwt)).build();
    }
}
