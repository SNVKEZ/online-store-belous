package org.ssu.belous.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssu.belous.dto.request.RegistrationRequestDto;
import org.ssu.belous.exception.UserAlreadyExistException;
import org.ssu.belous.model.User;
import org.ssu.belous.repository.UserRepository;

import java.util.UUID;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registration(RegistrationRequestDto requestDto){
        if(userRepository.existsByUsername(requestDto.username())) throw new UserAlreadyExistException("Пользователь уже существует");

        User user = User.builder()
                .uuid(UUID.randomUUID())
                .username(requestDto.username())
                .password(passwordEncoder.encode(requestDto.password()))
                .role(requestDto.role())
                .build();

        userRepository.save(user);
    }
}
