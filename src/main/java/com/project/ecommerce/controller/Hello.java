package com.project.ecommerce.controller;

import com.project.ecommerce.dto.UserDetails;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.ecommerce.dto.UserDetails;

@RestController
public class Hello {

    @GetMapping("/")
    public String hello() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getId() + " " + userDetails.getRole());
        //check token
        //get it from barrier
        return "Hello World!";
    }
}
