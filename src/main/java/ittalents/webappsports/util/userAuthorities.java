package ittalents.webappsports.util;

import ittalents.webappsports.exceptions.NotAdminException;
import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.User;

import javax.servlet.http.HttpSession;


public class userAuthorities {
    public static void validateUser(HttpSession session) throws UserNotLoggedException {
        if(session.getAttribute("Logged") == null){
            throw new UserNotLoggedException();
        }
    }
    public static void validateAdmin(HttpSession session) throws UserNotLoggedException, NotAdminException {
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

    public static boolean isAdmin(User user){
        if(user.getRoleId() == 0){
            return true;
        }
        return false;
    }
}
