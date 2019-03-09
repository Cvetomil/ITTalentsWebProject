package ittalents.webappsports.util;

import ittalents.webappsports.controllers.SportalController;
import ittalents.webappsports.exceptions.NotAdminException;
import ittalents.webappsports.exceptions.UserException;
import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.User;

import javax.servlet.http.HttpSession;


public class userAuthorities extends SportalController {
    public static User validateUser(HttpSession session) throws UserNotLoggedException {
        if (session.getAttribute("Logged") == null) {
            throw new UserNotLoggedException("User not logged");
        }
        return (User)session.getAttribute("Logged");
    }

    public static User validateAdmin(HttpSession session) throws UserNotLoggedException, NotAdminException {
        if (session.getAttribute("Logged") == null) {
            throw new UserNotLoggedException("User not logged");
        }
        User loggedUser = (User) session.getAttribute("Logged");
        if (!isAdmin(loggedUser)) {
            throw new NotAdminException("You are not admin");
        }
        return loggedUser;
    }

    public static void verifiedAcc(User user) throws UserException {
        if (!user.isConfirmed()) {
            throw new UserException("User account not verified");
        }

    }

    private static boolean isAdmin(User user) {
        if (user.getRoleId() == 0) {
            return true;
        }
        return false;
    }


}
