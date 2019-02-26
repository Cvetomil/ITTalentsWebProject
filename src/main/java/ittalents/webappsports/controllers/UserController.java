package ittalents.webappsports.controllers;


import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user){
        userRepository.save(user);
        return "You have been registered successfully";
    }

}
