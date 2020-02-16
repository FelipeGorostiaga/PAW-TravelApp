package ar.edu.itba.paw.webapp.controller;


import org.springframework.stereotype.Controller;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("activities")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class ActivityControllerREST {

}
