package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.CommentDislike;
import ittalents.webappsports.models.CommentLike;
import ittalents.webappsports.repositories.CommentDislikeRepository;
import ittalents.webappsports.repositories.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class CommentLikeController {

    @Autowired
    CommentLikeRepository clr;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    CommentDislikeRepository commentDislikeRepository;


    @PostMapping("/articles/comments/{commentId}/like")
    public void likeComment (HttpSession session, @PathVariable long commentId)throws UserNotLoggedException{
        if (session.getAttribute("Logged") == null) {
            throw new UserNotLoggedException();
        }
        CommentLike like = new CommentLike();
        CommentLike.CommentLikeId commentLikeId = new CommentLike.CommentLikeId();
        commentLikeId.setCommentId(commentId);
        commentLikeId.setUserId((long) (session.getAttribute("userId")));
        like.setId(commentLikeId);
        clr.save(like);

        CommentDislike.CommentDislikeId commentDislikeId = new CommentDislike.CommentDislikeId();
        commentDislikeId.setCommentId(commentId);
        commentDislikeId.setUserId((long) (session.getAttribute("userId")));

        CommentDislike commentDislike = commentDislikeRepository.findById(commentDislikeId);
        if (commentDislike != null) {
            commentDislikeRepository.delete(commentDislike);
        }
    }
}
