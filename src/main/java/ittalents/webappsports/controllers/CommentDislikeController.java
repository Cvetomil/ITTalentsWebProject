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
public class CommentDislikeController extends SportalController{

    @Autowired
    CommentLikeRepository commentLikeRepository;
    @Autowired
    CommentDislikeRepository commentDislikeRepository;

    @Transactional
    @PostMapping("/articles/{articleId}/comments/{commentId}/dislike")
    public void dislikeComment(HttpSession session, @PathVariable long commentId, @PathVariable long articleId)
            throws UserNotLoggedException, BadRequestException {
        validateEligibility(session, articleId, commentId);
        long userId = getUser(session).getId();
        CommentDislike dislike = new CommentDislike();
        dislike.setCommentId(commentId);
        dislike.setUserId(userId);
        CommentDislike isAlreadyDisliked = commentDislikeRepository.findByCommentIdAndUserId(commentId, userId);
        if (isAlreadyDisliked != null) {
            commentDislikeRepository.delete(isAlreadyDisliked);
        } else {
            commentDislikeRepository.save(dislike);
        }

        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
        if (commentLike != null) {
            commentLikeRepository.delete(commentLike);
        }
    }
}
