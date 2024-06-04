package ru.gudkova.mongoproject.controllers;                            

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/security")
public class SecurityController {
    @GetMapping("/login")
    public String login() {
        return "login/login";
    }
}
