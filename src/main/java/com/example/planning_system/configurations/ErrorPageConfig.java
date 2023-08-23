package com.example.planning_system.configurations;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.boot.web.servlet.error.ErrorController;

@Configuration
public class ErrorPageConfig implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError() {
        return new ModelAndView("error-page");
    }

}
