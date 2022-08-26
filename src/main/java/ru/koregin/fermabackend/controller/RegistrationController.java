package ru.koregin.fermabackend.controller;

import lombok.extern.slf4j.Slf4j;
import ru.koregin.fermabackend.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.koregin.fermabackend.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping()
    ResponseEntity<User> registration(@RequestBody Map<String, String> payload) {
        log.info("---- Создание нового пользователя ----");
        String username = payload.get("username");
        String password = payload.get("password");
        User newUser = new User();
        Optional<User> existUser = userRepo.findByUsername(username);
        if (existUser.isPresent()) {
            log.info("Пользователь " + existUser.get().getUsername() + " уже существует!");
            newUser.setUsername(existUser.get().getUsername());
            newUser.setPassword(existUser.get().getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(newUser);
        }
        log.info("Создаю нового пользователя " + username);
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        return ResponseEntity.status(HttpStatus.OK).body(userRepo.save(newUser));
    }
}
