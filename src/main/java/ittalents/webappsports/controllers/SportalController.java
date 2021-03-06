package ittalents.webappsports.controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import ittalents.webappsports.exceptions.*;
import ittalents.webappsports.models.Article;
import ittalents.webappsports.models.Comment;
import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.ArticleRepository;
import ittalents.webappsports.repositories.CommentRepository;
import ittalents.webappsports.util.MsgResponse;
import ittalents.webappsports.util.userAuthorities;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.util.Optional;

@ControllerAdvice
public class SportalController {

    @Autowired
    ArticleRepository ar;
    @Autowired
    CommentRepository cr;


    static Logger log = Logger.getLogger(SportalController.class.getName());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserException.class})
    public MsgResponse handleUserException(UserException e){
        log.error(e.getMessage());
        return new MsgResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UserNotLoggedException.class})
    public MsgResponse handleUserException(UserNotLoggedException e) {
        log.error(e.getMessage());
        return new MsgResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({NotAdminException.class})
    public MsgResponse handleNotAdminException(NotAdminException e) {
        log.error(e.getMessage());
        return new MsgResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({EmailAlreadyExist.class})
    public MsgResponse sameEmail(EmailAlreadyExist e) {
        log.error(e.getMessage());
        return new MsgResponse(HttpStatus.CONFLICT.value(),e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({UsernameAlreadyExist.class})
    public MsgResponse sameUsername(UsernameAlreadyExist e) {
        log.error(e.getMessage());
        return new MsgResponse(HttpStatus.CONFLICT.value(),e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({WrongCredentialsException.class})
    public MsgResponse wrongUsernameOrPassword(WrongCredentialsException e) {
        log.error(e.getMessage());
        return new MsgResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage());
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler({MessagingException.class})
    public MsgResponse handleEmailException(MessagingException e) {
        log.error(e.getMessage());
        return new MsgResponse(HttpStatus.EXPECTATION_FAILED.value(),e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class, FileNotFoundException.class})
    public MsgResponse handleNotFoundException(Exception e) {
        log.error(e.getMessage());
        return new MsgResponse(HttpStatus.NOT_FOUND.value(),"File not found");
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MultipartException.class)
    public MsgResponse handleMultipartException(MultipartException e) {
        log.error(e.getMessage());
        return new MsgResponse(HttpStatus.SERVICE_UNAVAILABLE.value(),e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class, NumberFormatException.class,
            InvalidFormatException.class,
            HttpMediaTypeNotSupportedException.class, MismatchedInputException.class})
    public MsgResponse handleBadRequestsExceptions(Exception e) {
        log.error(e.getMessage());
        return new MsgResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage());
    }

     public Article checkArticlePresence(long articleId) throws BadRequestException {
        Optional<Article> article = ar.findById(articleId);
        if (!article.isPresent()) {
            throw new BadRequestException("Article not found!");
        }
        return article.get();
    }

    public void validateArticleAuthor(User admin, Article article)
            throws BadRequestException {
                if (!admin.getUsername().equals(article.getAuthor())) {
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

    private void validateIfCommentBelongsToArticle (Article article, Comment comment)
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
    public MsgResponse handleMediaExceptions(MediaException e){
        log.error(e.getMessage());
        return new MsgResponse(HttpStatus.EXPECTATION_FAILED.value(),e.getMessage());
    }
}
