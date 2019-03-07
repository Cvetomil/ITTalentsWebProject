package ittalents.webappsports.controllers;


import ittalents.webappsports.dto.UserDTO;
import ittalents.webappsports.exceptions.*;
import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.UserRepository;
import ittalents.webappsports.util.EmailSender;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Optional;

import static ittalents.webappsports.util.userAuthorities.validateUser;


@RestController
public class UserController extends SportalController{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    static Logger log = Logger.getLogger(UserController.class.getName());

    //register a user
    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody User user) throws UserException {
        emailExist(user.getEmail());
        usernameExist(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        new Thread(() -> {
            try {
                EmailSender.sendEmail(user.getEmail(),"registration","You have been registered");
            } catch (MessagingException e) {
                System.out.println("Something went wrong with the mail service");
            }
        }).start();
        log.info("user registered");
        return new UserDTO().convertToDTO(user);
    }

    // checks if email exist in database
    private void emailExist(String email) throws EmailAlreadyExist {
        User user = userRepository.getByEmail(email);
        if(user != null){
            throw new EmailAlreadyExist("email already exist");
        }
    }
    // checks if username exist in database
    private void usernameExist(String username) throws UsernameAlreadyExist {
        User user = userRepository.getByUsername(username);
        if(user != null){
            throw new UsernameAlreadyExist("username already exist");
        }
    }

    //login
    @PostMapping("/login")
    public UserDTO login(@RequestBody User user, HttpSession session) throws UserException{
        User userToJson = userRepository.getByUsername(user.getUsername());
        if(userToJson == null){
            throw new WrongCredentialsException("wrong username or password");
        }
        else {
            if (encoder.matches(user.getPassword(), userToJson.getPassword())) {

                session.setAttribute("Logged", userToJson);
               session.setAttribute("userId", userToJson.getId());
               session.setAttribute("roleId", userToJson.getRoleId());
               session.setAttribute("commentTime", LocalTime.MIN);
                return new UserDTO().convertToDTO(userToJson);
            }
            throw new WrongCredentialsException("wrong username or password");
        }
    }

    //logout from session
    @PostMapping("/logout")
    public void logout(HttpSession session){
        session.invalidate();
    }

    //edit password on logged user
    @PutMapping("/user/edit/password")
    public UserDTO changePassword(@RequestBody User user, HttpSession session) throws UserException, SQLException,NotFoundException {
        User userFromDB = getUserFromSession(session);
        userFromDB.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(userFromDB);
        return new UserDTO().convertToDTO(user);
    }
    //edit username on logged user
    @PutMapping("/user/edit/username")
    public User changeUsername(@RequestBody User user, HttpSession session) throws UserException, NotFoundException {
        User userFromDB = getUserFromSession(session);
        userFromDB.setUsername(user.getUsername());
        userRepository.save(userFromDB);
        return user;
    }
    private User getUserFromSession(HttpSession session) throws UserNotLoggedException, NotFoundException {
        validateUser(session);
        long userId = (long)session.getAttribute("userId");
        Optional<User> u = userRepository.findById(userId);
        if(!u.isPresent()){
            throw new NotFoundException("User not found");
        }
        return u.get();
    }


}
