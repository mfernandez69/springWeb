package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping
    public String index() {
        return "index";
    }

    @PostMapping("/clients")
    public String redirectToClients() {
        return "redirect:/clients";
    }

    @PostMapping("/products")
    public String redirectToProducts() {
        return "redirect:/products";
    }
}
