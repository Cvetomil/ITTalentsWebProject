package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.*;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.MessagingException;

@ControllerAdvice
public class SportalController {


    static Logger log = Logger.getLogger(SportalController.class.getName());


    @ExceptionHandler({UserNotLoggedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String handleUserException() {
        log.error("User not logged exception");
        return "You are not logged in!";
    }

    @ExceptionHandler({NotAdminException.class})
    public String handleNotAdminException() {
        log.error("User not admin exception");
        return "You are not admin!";
    }

    @ExceptionHandler({EmailAlreadyExist.class})
    public String sameEmail() {
        log.error("email exist exception");
        return "User with that email already exist";
    }

    @ExceptionHandler({UsernameAlreadyExist.class})
    public String sameUsername() {
        log.error("same username exception");
        return "User with that username already exist";
    }

    @ExceptionHandler({WrongCredentialsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String wrongUsernameOrPassword() {
        log.error("wrong username or password exception");
        return "Wrong username or password";
    }

    @ExceptionHandler({MessagingException.class})
    public String handleEmailException() {
        log.error("mail service exception");
        return "Something went wrong with the mail service";
    }


    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception e) {
        log.error("Not found exception");
        return e.getMessage();
    }

    @ExceptionHandler(TeaPotException.class)
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    public String handleTeaPotException(Exception e) {
        log.error("Teapot exception");
        return e.getMessage();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleBadRequestsExceptions(Exception e) {
        log.error("Bad request exception");
        return e.getMessage();

    }
}
