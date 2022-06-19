package ru.corruptzero.alert.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlertAppController {
    @GetMapping
    public String client() {
        return "client";
    }


}
