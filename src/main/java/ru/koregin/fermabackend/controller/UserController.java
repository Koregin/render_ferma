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
import ru.koregin.fermabackend.model.UserDTO;
import ru.koregin.fermabackend.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/register")
public class UserController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping()
    ResponseEntity<UserDTO> registration(@RequestBody Map<String, String> payload) {
        log.info("---- Создание нового пользователя ----");
        String username = payload.get("username");
        String password = payload.get("password");
        Optional<User> existUser = userRepo.findByUsername(username);
        if (existUser.isPresent()) {
            log.info("Пользователь " + existUser.get().getUsername() + " уже существует!");
            return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(username));
        }
        log.info("Создаю нового пользователя " + username);
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepo.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(username));
    }
}
