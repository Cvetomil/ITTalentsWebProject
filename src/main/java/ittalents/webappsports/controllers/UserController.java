package ittalents.webappsports.controllers;


import ittalents.webappsports.dto.UserDTO;
import ittalents.webappsports.exceptions.*;
import ittalents.webappsports.util.EmailSender;
import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static ittalents.webappsports.util.userAuthorities.validateUser;


@RestController
public class UserController extends SportalController{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public void registerUser(@RequestBody User user) throws UserException, MessagingException {
        emailExist(user.getEmail());
        usernameExist(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        EmailSender.sendEmail(user.getEmail(), "Congrats " + user.getUsername() + ", you have been registered!");
    }

    private void emailExist(String email) throws EmailAlreadyExist {
        String sqlEmail = "SELECT count(*) FROM users WHERE email = ?";
        int countEmails = jdbcTemplate.queryForObject(sqlEmail, new Object[] { email }, Integer.class);

        if(countEmails > 0){ //if user with the same email exists in database
            throw new EmailAlreadyExist();
        }
    }
    private void usernameExist(String username) throws UsernameAlreadyExist {

        String sqlUsername = "SELECT count(*) FROM users WHERE username = ?";
        int countUsernames = jdbcTemplate.queryForObject(sqlUsername, new Object[] { username }, Integer.class);

        if(countUsernames > 0){ //if user with the same username exists
            throw new UsernameAlreadyExist();
        }
    }
    @PostMapping("/login")
    public UserDTO login(@RequestBody User user, HttpSession session) throws UserException{
        User userToJson = userRepository.getByUsername(user.getUsername());
        if(userToJson == null){
            throw new WrongCredentialsException();
        }
        else{
            if(encoder.matches(user.getPassword(),userToJson.getPassword())) {
                session.setAttribute("Logged", userToJson);
                return new UserDTO().convertToDTO(userToJson);
            }
            else {
                throw new WrongCredentialsException();
            }
        }
    }
    @PostMapping("/logout")
    public void logout(HttpSession session){
        session.setAttribute("Logged", null);
    }
    @PostMapping("/user/edit/password")
    public void changePassword(@RequestBody User user, HttpSession session) throws UserException, SQLException {
        validateUser(session);
        PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement("UPDATE users SET password = ? WHERE id = ?");
        ps.setString(1, encoder.encode(user.getPassword()));
        User sameLoggedUser = (User)session.getAttribute("Logged");
        ps.setLong(2,sameLoggedUser.getId());
        ps.executeUpdate();
    }
    @PostMapping("/user/edit/username")
    public void changeUsername(@RequestBody User user, HttpSession session) throws UserException, SQLException {
        validateUser(session);
        PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement("UPDATE users SET username = ? WHERE id = ?");
        ps.setString(1, user.getUsername());
        User sameLoggedUser = (User)session.getAttribute("Logged");
        ps.setLong(2,sameLoggedUser.getId());
        ps.executeUpdate();
    }



}
