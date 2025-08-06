package com.project.ecommerce.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @GetMapping("/")
    public String hello() {
        Long id = (Long)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(id);
        return "Hello World!";
    }
}
