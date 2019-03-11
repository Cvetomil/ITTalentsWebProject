package ittalents.webappsports.controllers;


import ittalents.webappsports.dto.UserDTO;
import ittalents.webappsports.exceptions.*;
import ittalents.webappsports.models.ConfirmationToken;
import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.ConfirmationTokenRep;
import ittalents.webappsports.repositories.UserRepository;
import ittalents.webappsports.util.EmailSender;
import ittalents.webappsports.util.MsgResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Optional;

import static ittalents.webappsports.util.userAuthorities.validateUser;
import static ittalents.webappsports.util.userAuthorities.verifiedAcc;


@RestController
public class UserController extends SportalController{
    private static final String PASS_REGEX = "((?=.*[a-z])(?=.*d)(?=.*[@#$%!?^&*-_])(?=.*[A-Z]).{6,20})";
    private static final String EMAIL_REGEX =
            "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9]" +
            "(?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    private static final String VERIFICATION_URL = "http://localhost:8080/confirm-account-code=";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConfirmationTokenRep confirmationTokenRep;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    static Logger log = Logger.getLogger(UserController.class.getName());

    //register a user
    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody User user) throws UserException, BadRequestException {
        verifyGivenData(user);
        verifyPassword(user.getPassword());
        emailExist(user.getEmail());
        usernameExist(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRep.save(confirmationToken);
        sendVerificationEmail(user,confirmationToken);
        return new UserDTO().convertToDTO(user);
    }

    //verify if user entered everything correct
    private void verifyGivenData(User user) throws BadRequestException {
        if(user.getAge() < 1 || user.getAge() > 120){
            throw new BadRequestException("Enter correct age");
        }
        if(user.getEmail() == null){
            throw new BadRequestException("You need to enter an email");
        }
        if(!user.getEmail().matches(EMAIL_REGEX)){
            throw new BadRequestException("enter a valid email address");
        }
        if(user.getPassword() == null){
            throw new BadRequestException("You need to enter a password");
        }
        if(user.getUsername() == null){
            throw new BadRequestException("You need to input username");
        }
        if(!user.getPassword().equals(user.getConfirmPassword())){
            throw new BadRequestException("passwords do not match");
        }
    }
    public void verifyPassword(String password) throws BadRequestException {
        if(!password.matches(PASS_REGEX)) {
            throw new BadRequestException("password must be at least 6 characters long," +
                    " have at least one digit," +
                    " a lower case letter ," +
                    "an upper case letter and a special character");
        }
    }
    private void sendVerificationEmail(User user, ConfirmationToken confirmationToken){
        new Thread(() -> {
            try {
                EmailSender.sendEmail(
                        user.getEmail(),
                        "Confirm your account",
                        "Please follow this link to confirm your account: "+ VERIFICATION_URL + confirmationToken.getCode());
            } catch (MessagingException e) {
                System.out.println("Something went wrong with the mail service");
            }
        }).start();
    }

    // checks if email exist in database
    private void emailExist(String email) throws EmailAlreadyExist {
        User user = userRepository.getByEmail(email);
        if(user != null){
            throw new EmailAlreadyExist("email already exist");
        }
    }

    //enable user account
    @GetMapping(value="/confirm-account-code={code}")
    public String confirmUserAccount(@PathVariable String code) throws NotFoundException {
        ConfirmationToken token = confirmationTokenRep.findByCode(code);

        if(token != null){
            User user = userRepository.getByEmail(token.getUser().getEmail());
            user.setConfirmed(true);
            userRepository.save(user);
            return "registration confirmed";
        }
        else{
            throw new NotFoundException("Token not found");
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
            verifiedAcc(userToJson); // checks if acc is enabled
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
    public MsgResponse logout(HttpSession session){
        session.invalidate();
        return new MsgResponse(HttpStatus.OK.value(),"logout successful");
    }

    //edit password on logged user
    @PutMapping("/user/edit/password")
    public UserDTO changePassword(@RequestBody User user, HttpSession session) throws UserException , NotFoundException, BadRequestException {
        User userFromDB = getUserFromSession(session);
        verifyPassword(user.getPassword());
        userFromDB.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(userFromDB);
        return new UserDTO().convertToDTO(userFromDB);
    }
    //edit username on logged user
    @PutMapping("/user/edit/username")
    public UserDTO changeUsername(@RequestBody User user, HttpSession session) throws UserException, NotFoundException {
        usernameExist(user.getUsername());
        User userFromDB = getUserFromSession(session);
        userFromDB.setUsername(user.getUsername());
        userRepository.save(userFromDB);
        return new UserDTO().convertToDTO(userFromDB);
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
