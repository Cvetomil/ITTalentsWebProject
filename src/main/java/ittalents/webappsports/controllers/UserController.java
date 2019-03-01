package ittalents.webappsports.controllers;


import ittalents.webappsports.exceptions.*;
import ittalents.webappsports.util.EmailSender;
import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;


@RestController
public class UserController extends SportalController{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public void registerUser(@RequestBody User user) throws UserException, MessagingException {
        EmailOrUsernameExist(user.getEmail(), user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        EmailSender.sendEmail(user.getEmail(), "Congrats " + user.getUsername() + ", you have been registered!");
    }

    public void EmailOrUsernameExist(String email,String username) throws UserException{
        String sqlEmail = "SELECT count(*) FROM users WHERE email = ?";
        int countEmails = jdbcTemplate.queryForObject(sqlEmail, new Object[] { email }, Integer.class);
        String sqlUsername = "SELECT count(*) FROM users WHERE username = ?";
        int countUsernames = jdbcTemplate.queryForObject(sqlUsername, new Object[] { username }, Integer.class);

        if(countEmails > 0){ //if user with the same email exists in database
            throw new EmailAlreadyExist();
        }
        else if(countUsernames > 0){ //if user with the same username exists
             throw new UsernameAlreadyExist();
        }
    }

    @PostMapping("/login")
    public User login(@RequestBody User user, HttpSession session) throws UserException{
        User userToJson = userRepository.getByUsername(user.getUsername());
        if(userToJson == null){
            throw new WrongCredentialsException();
        }
        else{
            if(encoder.matches(user.getPassword(),userToJson.getPassword())) {
                session.setAttribute("Logged", user);
                return userToJson;
            }
            else {
                throw new WrongCredentialsException();
            }
        }
    }



}
