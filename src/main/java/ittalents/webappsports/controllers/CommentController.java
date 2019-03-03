package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.TeaPotException;
import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.Comment;
import ittalents.webappsports.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CommentController extends SportalController{

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/comment/{id}")
    public Comment getCommentById(@PathVariable long id) throws NoSuchFieldException{
        if(commentRepository.existsById(id)) {
            return commentRepository.findById(id).get();
        }
        throw new NoSuchFieldException();
    }
    @GetMapping("/comment/mostLiked")
    public List<Comment> getMostLikedComments(){
        List<Comment> comments = jdbcTemplate.query("SELECT * FROM comments WHERE likes = (SELECT MAX(likes) FROM comments)",(resultSet,i) -> {return toComment(resultSet);});
        return comments;
    }

    public Comment toComment(ResultSet rs){
        //TODO Insert data to comment and return it
        return null;
    }

    @GetMapping("/comment/all")
    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }


//adding a comment
    @PostMapping("/articles/{id}/addcomment")
    public void addComment(@RequestBody String text, HttpSession session, @PathVariable long id) throws UserNotLoggedException {
       if (session.getAttribute("Logged") == null){
           throw new UserNotLoggedException() ;
       } else {
           Comment comment = new Comment();
           comment.setArtId(id);
           comment.setUserId((long) session.getAttribute( "userId"));
           comment.setText(text);
           commentRepository.save(comment);
       }

    }

    //delete a comment
        @PutMapping("/articles/{id}/deletecomment{commentId}")
    public void deleteComment (HttpSession session, @PathVariable long id, @PathVariable long commentId) throws TeaPotException, UserNotLoggedException{
        if (session.getAttribute("Logged") == null) {
            throw new UserNotLoggedException();
        }
        Comment comment = commentRepository.getOne(commentId);
        if (comment.getArtId() != id){
            throw new TeaPotException("Don't do this!");
                    }
                commentRepository.delete(comment);
    }

    //editing a comment
    @PutMapping("/articles/{id}/editcomment{commentId}")
    public void editComment (HttpSession session,@RequestBody String text, @PathVariable long id, @PathVariable long commentId) throws TeaPotException, UserNotLoggedException{
        if (session.getAttribute("Logged") == null) {
            throw new UserNotLoggedException();
        }

        Comment comment = commentRepository.getOne(commentId);
        if (comment.getArtId() != id){
            throw new TeaPotException("Don't do this!");
        }

        comment.setEdited(true);
        comment.setLastEdited(LocalDateTime.now());
        comment.setText(text);
        commentRepository.save(comment);
    }

    //delete all comments for a user
    @PutMapping("/users/deleteallcomments")
    public void deleteAllComments (HttpSession session) throws UserNotLoggedException{


        if (session.getAttribute("Logged") == null) {
            throw new UserNotLoggedException();
        }
        
               String userId = String.valueOf((long) session.getAttribute("userId"));
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM comments WHERE user_id = ");
        sql.append(userId);

        jdbcTemplate.execute(sql.toString());
    }

    @ExceptionHandler(NoSuchFieldException.class)
    public String handleIdNotFoundException(){
        return "comment does not exist";
    }


}
