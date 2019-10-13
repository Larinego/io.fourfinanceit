package io.fourfinanceit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import io.fourfinanceit.model.User;
import io.fourfinanceit.repository.UserRepository;
import io.fourfinanceit.security.Roles;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    private Map<String, User> tokens = new ConcurrentHashMap<>();

    @Override
    public String saveUser(User user) throws Exception {

        user.setRole(Roles.USER_ROLE);

        if(user.getUsername().length() < 3){
            throw new Exception("User must have at least 3 characters at username!");
        }

        if(user.getPassword().length() < 3){
            throw new Exception("Password must be longer then 3 characters!");
        }

        userRepository.save(user);
        return generateAndSaveToken(user);
    }

    @Override
    public String login(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username,password);
        if(user.isPresent()){
            return generateAndSaveToken(user.get());
        }
        return "";
    }

    @Override
    public Optional<org.springframework.security.core.userdetails.User> findByToken(String token) {

        org.springframework.security.core.userdetails.User user = null;

        Optional<User> optUser = Optional.ofNullable(tokens.get(token));
        if(optUser.isPresent()){
            User loanUser = optUser.get();
            user= new org.springframework.security.core.userdetails.User(loanUser.getUsername(), loanUser.getPassword(), true, true, true, true,
                AuthorityUtils.createAuthorityList(loanUser.getRole()));
        }
        Optional<org.springframework.security.core.userdetails.User> result = Optional.ofNullable(user);
        return  result;
    }

    @Override
    public User getCurrentUser() throws Exception{
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Optional<User> user = findByUsername(username);
        if (user.isPresent()){
            return user.get();
        }
        else{
            throw new Exception("User not found!");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    private String generateAndSaveToken(User user){
        String token = UUID.randomUUID().toString();
        tokens.put(token, user);
        return token;
    }

}
