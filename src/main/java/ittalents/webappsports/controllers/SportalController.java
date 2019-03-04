package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.MessagingException;

@ControllerAdvice
public class SportalController {
    @ExceptionHandler({UserNotLoggedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String handleUserException(){
        return "You are not logged in!";
    }

    @ExceptionHandler({NotAdminException.class})
    public String handleNotAdminException(){
        return "You are not admin!";
    }
    @ExceptionHandler({EmailAlreadyExist.class})
    public String sameEmail(){
        return "User with that email already exist";
    }
    @ExceptionHandler({UsernameAlreadyExist.class})
    public String sameUsername(){
        return "User with that username already exist";
    }
    @ExceptionHandler({WrongCredentialsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String wrongUsernameOrPassword(){
        return "Wrong username or password";
    }
        @ExceptionHandler({MessagingException.class})
    public String handleEmailException(){
        return "Something went wrong with the mail service";
    }


    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception e){
        return e.getMessage();
    }

    @ExceptionHandler(TeaPotException.class)
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    public String handleTeaPotException (Exception e){
        return e.getMessage();
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleBadRequestsExceptions (Exception e){
        return "Bad request!";
    }
}
