package ittalents.webappsports.controllers;

import ittalents.webappsports.dto.CommentDTO;
import ittalents.webappsports.exceptions.BadRequestException;
import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.Comment;
import ittalents.webappsports.models.User;
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
    public Comment addComment(@RequestBody CommentDTO commentDTO, HttpSession session, @PathVariable long articleId)
            throws UserNotLoggedException, BadRequestException {
        User user = userAuthorities.validateUser(session);
        checkArticlePresence(articleId);
        LocalTime lastCommentTime = (LocalTime) session.getAttribute("commentTime");
        if (lastCommentTime.plusMinutes(1).isAfter(LocalTime.now())){
            throw new BadRequestException("You have already commented in the last minute!");
        }
        Comment comment = new Comment();
        comment.setArtId(articleId);
        comment.setUserId(user.getId());
        comment.setText(commentDTO.getText());
        cr.save(comment);
        session.setAttribute("commentTime", LocalTime.now());
        return comment;
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
    public Comment editComment(HttpSession session, @RequestBody CommentDTO commentDTO,
                            @PathVariable long articleId, @PathVariable long commentId)
            throws BadRequestException, UserNotLoggedException {
        Comment comment = validateEligibility(session, articleId, commentId);
        validateCommentAuthor(session, commentId);
                        comment.setEdited(true);
        comment.setLastEdited(LocalDateTime.now());
        comment.setText(commentDTO.getText());
        cr.save(comment);
        return comment;
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
