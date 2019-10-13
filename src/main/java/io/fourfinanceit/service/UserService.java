package io.fourfinanceit.service;

import io.fourfinanceit.model.User;

import java.util.Optional;

public interface UserService {

    String saveUser(User user) throws Exception;

    String login(String username, String password);

    Optional<User> findByUsername(String username);

    Optional<org.springframework.security.core.userdetails.User> findByToken(String token);

    public User getCurrentUser() throws Exception;



}
