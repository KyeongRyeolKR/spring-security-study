package com.example.security.controller;

import com.example.security.dto.JoinDto;
import com.example.security.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class JoinController {

    private final JoinService joinService;

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(JoinDto dto) {
        log.info("username={}", dto.getUsername());
        log.info("password={}", dto.getPassword());

        joinService.joinProcess(dto);

        return "redirect:/login";
    }
}
