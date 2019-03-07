package ittalents.webappsports.controllers;

import ittalents.webappsports.dto.EditedCommentDTO;
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
import java.time.LocalTime;

@RestController
public class CommentController extends SportalController {

    @Autowired
    CommentRepository cr;

    //adding a comment
    @PostMapping("/articles/{articleId}/comment")
    public void addComment(@RequestBody String text, HttpSession session, @PathVariable long articleId)
            throws UserNotLoggedException, BadRequestException {
        userAuthorities.validateUser(session);
        checkArticlePresence(articleId);
        LocalTime lastCommentTime = (LocalTime) session.getAttribute("commentTime");
        if (lastCommentTime.plusMinutes(1).isAfter(LocalTime.now())){
            throw new BadRequestException("You have already commented in the last minute!");
        }
        Comment comment = new Comment();
        comment.setArtId(articleId);
        comment.setUserId(getUser(session).getId());
        comment.setText(text);
        cr.save(comment);
        session.setAttribute("commentTime", LocalTime.now());
    }

    //delete a comment
        @DeleteMapping("/articles/{articleId}/comment/{commentId}")
    public void deleteComment(HttpSession session, @PathVariable long commentId, @PathVariable long articleId)
            throws BadRequestException, UserNotLoggedException {
        Comment comment = validateEligibility(session, articleId, commentId);
        validateCommentAuthor(session, commentId);
                cr.delete(comment);

            }

    //editing a comment
    @PutMapping("/articles/{articleId}/comment/{commentId}")
    public void editComment(HttpSession session, @RequestBody EditedCommentDTO editedCommentDTO,
                            @PathVariable long articleId, @PathVariable long commentId)
            throws BadRequestException, UserNotLoggedException {
                validateEligibility(session, articleId, commentId);
        validateCommentAuthor(session, commentId);
                Comment comment = cr.getOne(commentId);
        comment.setEdited(true);
        comment.setLastEdited(LocalDateTime.now());
        comment.setText(editedCommentDTO.getText());
        cr.save(comment);
    }

    //delete all comments for a user
    @DeleteMapping("/users/comments")
    @Transactional
        public void deleteAllComments(HttpSession session) throws UserNotLoggedException {
        userAuthorities.validateUser(session);
        long userId = getUser(session).getId();
        cr.removeAllByUserId(userId);
    }
}
