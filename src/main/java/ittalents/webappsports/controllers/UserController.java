package ittalents.webappsports.controllers;


import ittalents.webappsports.exceptions.UserNotFoundException;
import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
public class UserController extends SportalController{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public void registerUser(@RequestBody User user){
            userRepository.save(user);
    }
    @ExceptionHandler({ConstraintViolationException.class})
    public String userNotFoundException(){
        return "User not Found!";
    }
    @PostMapping("/login")
    public User login(@RequestBody User user, HttpSession session) throws UserNotFoundException{
        User userToJson = userRepository.getByUsername(user.getUsername());
        if(userToJson == null){
            throw new UserNotFoundException();
        }
        else{
            session.setAttribute("Logged",user);
            return userToJson;
        }
    }



}
