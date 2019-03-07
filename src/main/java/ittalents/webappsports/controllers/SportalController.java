package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.*;
import ittalents.webappsports.models.Article;
import ittalents.webappsports.models.Comment;
import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.ArticleRepository;
import ittalents.webappsports.repositories.CommentRepository;
import ittalents.webappsports.util.userAuthorities;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@ControllerAdvice
public class SportalController {

    @Autowired
    ArticleRepository ar;
    @Autowired
    CommentRepository cr;


    static Logger log = Logger.getLogger(SportalController.class.getName());


    @ExceptionHandler({UserException.class})
    public String handleUserException(UserException e){
        log.error("user exception");
        return e.getMessage();
    }
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

    public Article checkArticlePresence(long articleId) throws BadRequestException {
        Optional<Article> article = ar.findById(articleId);
        if (!article.isPresent()) {
            throw new BadRequestException("Article not found!");
        }
        return article.get();
    }

    public void validateArticleAuthor(HttpSession session, long articleId)
            throws BadRequestException {
        User admin = getUser(session);
        if (!ar.getOne(articleId).getAuthor().equals(admin.getUsername())) {
            throw new BadRequestException("You are not the author of this article!");
        }
    }

    public Comment checkCommentPresence(long commentId) throws BadRequestException {
        Optional<Comment> comment = cr.findById(commentId);
        if (!cr.findById(commentId).isPresent()) {
            throw new BadRequestException("Comment not found!");
        }
        return comment.get();
    }

    public void validateCommentAuthor(HttpSession session, long commentId)
            throws BadRequestException{
        long userId = getUser(session).getId();
        long commentCreatorId = cr.getOne(commentId).getUserId();
        if (userId != commentCreatorId) {
            throw new BadRequestException("You are not the author of this comment!");
        }
    }

    public void validateIfCommentBelongsToArticle (Article article, Comment comment)
            throws BadRequestException{
        if (comment.getArtId() != article.getId()){
            throw new BadRequestException("Comment doesn't belong to this article!");
        }
    }

    public Comment validateEligibility (HttpSession session, long articleId, long commentId)
            throws UserNotLoggedException, BadRequestException{
        userAuthorities.validateUser(session);
        Article article = checkArticlePresence(articleId);
        Comment comment =  checkCommentPresence(commentId);
        validateIfCommentBelongsToArticle(article, comment);
        return comment;
    }

    public User getUser(HttpSession session) {
                return (User) session.getAttribute("Logged");
    }

    @ExceptionHandler(MediaException.class)
    public String handleMediaExceptions(Exception e){
        log.error(e.getMessage());
        return e.getMessage();
    }
}
