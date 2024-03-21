package com.example.security.service;

import com.example.security.dto.JoinDto;
import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void joinProcess(JoinDto dto) {
        if(userRepository.existsByUsername(dto.getUsername())) {
            return;
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // 비밀번호는 암호화 필수! -> BCrypt
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }
}
