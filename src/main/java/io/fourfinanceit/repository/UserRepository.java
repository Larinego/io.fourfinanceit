package io.fourfinanceit.repository;

import org.springframework.dao.DataAccessException;
import io.fourfinanceit.model.User;

import java.util.Optional;

public interface UserRepository {

    void save(User user) throws DataAccessException;

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password) throws DataAccessException;

}
