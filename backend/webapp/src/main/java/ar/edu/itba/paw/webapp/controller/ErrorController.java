package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @RequestMapping("/403")
    public ModelAndView forbidden() {
        return new ModelAndView("403");
    }

    @RequestMapping("/404")
    public ModelAndView notFound() {
        return new ModelAndView("404");
    }

    @RequestMapping("/500")
    public ModelAndView unexpectedError() {
        return new ModelAndView("500");
    }

}
