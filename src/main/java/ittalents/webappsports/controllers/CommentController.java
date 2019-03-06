package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.BadRequestException;
import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.Comment;
import ittalents.webappsports.repositories.CommentRepository;
import ittalents.webappsports.util.userAuthorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
public class CommentController extends SportalController {

    @Autowired
    CommentRepository cr;

    //adding a comment
    @PostMapping("/articles/{articleId}/addcomment")
    public void addComment(@RequestBody String text, HttpSession session, @PathVariable long articleId)
            throws UserNotLoggedException, BadRequestException {
        userAuthorities.validateUser(session);
        checkArticlePresence(articleId);
        Comment comment = new Comment();
        comment.setArtId(articleId);
        comment.setUserId((long) session.getAttribute("userId"));
        comment.setText(text);
        cr.save(comment);
    }

    //delete a comment
    @DeleteMapping("/articles/{articleId}/deletecomment/{commentId}")
    public void deleteComment(HttpSession session, @PathVariable long commentId, @PathVariable long articleId)
            throws BadRequestException, UserNotLoggedException {
        userAuthorities.validateUser(session);
        checkArticlePresence(articleId);
        checkCommentPresence(commentId);
        validateCommentAuthor(session, commentId);
        validateIfCommentBelongsToArticle(articleId, commentId);
        cr.delete(cr.getOne(commentId));
    }

    //editing a comment
    @PutMapping("/articles/{articleId}/editcomment/{commentId}")
    public void editComment(HttpSession session, @RequestBody String text,
                            @PathVariable long articleId, @PathVariable long commentId)
            throws BadRequestException, UserNotLoggedException {
        userAuthorities.validateUser(session);
        checkArticlePresence(articleId);
        checkCommentPresence(commentId);
        validateCommentAuthor(session, commentId);
        validateIfCommentBelongsToArticle(articleId, commentId);
        Comment comment = cr.getOne(commentId);
        comment.setEdited(true);
        comment.setLastEdited(LocalDateTime.now());
        comment.setText(text);
        cr.save(comment);
    }

    //delete all comments for a user
    @DeleteMapping("/users/deleteallcomments")
    @Transactional
        public void deleteAllComments(HttpSession session) throws UserNotLoggedException {
        userAuthorities.validateUser(session);
        long userId = getUser(session).getId();
        cr.removeAllByUserId(userId);
    }
}
