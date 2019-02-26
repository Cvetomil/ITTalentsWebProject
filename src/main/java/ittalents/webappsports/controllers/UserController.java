package ittalents.webappsports.controllers;


import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
=======
import org.springframework.web.bind.annotation.PathVariable;
>>>>>>> e554c5be5135fcead1bdb82bf2b5d1d2d5e54b2a
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

<<<<<<< HEAD
    @PostMapping("/register")
    public String registerUser(@RequestBody User user){
        userRepository.save(user);
        return "You have been registered successfully";
    }

=======

    @PostMapping("/register")
        public void registerUser (@RequestBody User user){
        userRepository.save(user);

    }
>>>>>>> e554c5be5135fcead1bdb82bf2b5d1d2d5e54b2a
}
