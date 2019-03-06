package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.BadRequestException;
import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.CommentDislike;
import ittalents.webappsports.models.CommentLike;
import ittalents.webappsports.repositories.CommentDislikeRepository;
import ittalents.webappsports.repositories.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@RestController
public class CommentLikeController extends SportalController {

    @Autowired
    CommentLikeRepository commentLikeRepository;
    @Autowired
    CommentDislikeRepository commentDislikeRepository;

    @Transactional
    @PostMapping("/articles/{articleId}/comments/{commentId}/like")
    public void likeComment(HttpSession session, @PathVariable long commentId, @PathVariable long articleId)
            throws UserNotLoggedException, BadRequestException {
        validateEligibility(session, articleId, commentId);
        long userId = getUser(session).getId();
        CommentLike like = new CommentLike();
        like.setCommentId(commentId);
        like.setUserId(userId);
        CommentLike isAlreadyLiked = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
        if (isAlreadyLiked != null) {
            commentLikeRepository.delete(isAlreadyLiked);
        } else {
            commentLikeRepository.save(like);
        }

        CommentDislike commentDislike = commentDislikeRepository.findByCommentIdAndUserId(commentId, userId);
        if (commentDislike != null) {
            commentDislikeRepository.delete(commentDislike);
        }
    }
}
