package ru.koregin.fermabackend.service;

import lombok.extern.slf4j.Slf4j;
import ru.koregin.fermabackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.koregin.fermabackend.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Autowired
    public UserRepositoryUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Check user " + username + " in database");
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            log.info("User: " + user.get());
            return user.get();
        }
        throw new UsernameNotFoundException("User '" + username + "' not found");
    }
}