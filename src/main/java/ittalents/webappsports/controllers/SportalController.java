package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.NotAdminException;
import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.User;
import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
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
    @ExceptionHandler
    protected void validateUser(HttpSession session) throws UserNotLoggedException{
        if(session.getAttribute("Logged") == null){
            throw new UserNotLoggedException();
        }
    }
    protected void validateAdmin(HttpSession session) throws UserNotLoggedException, NotAdminException{
        if(session.getAttribute("Logged") == null){
            throw new UserNotLoggedException();
        }
        else{
            User loggedUser = (User)session.getAttribute("Logged");
            if(!isAdmin(loggedUser)){
                throw new NotAdminException();
            }
        }
    }
    private boolean isAdmin(User user){
        if(user.getRoleId() == 2){
            return false;
        }
        return true;
    }
}
