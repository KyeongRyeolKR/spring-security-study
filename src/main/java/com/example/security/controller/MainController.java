package com.example.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainP(Model model) {
        // 인증 객체
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String id = authentication.getName(); // username
        String role = authentication.getAuthorities().iterator().next().getAuthority(); // role

        model.addAttribute("id", id);
        model.addAttribute("role", role);

        return "main";
    }
}