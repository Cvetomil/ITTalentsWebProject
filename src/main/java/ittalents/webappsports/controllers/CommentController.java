package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.UserException;
import ittalents.webappsports.models.Comment;
import ittalents.webappsports.repositories.ArticleRepository;
import ittalents.webappsports.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.nio.file.NoSuchFileException;
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



    @PostMapping("/comment/add")
    public String addComment(@RequestBody Comment comment, HttpSession session) throws UserException {
        //validateUser(session);
        commentRepository.save(comment);
        return "comment saved";
    }

    @ExceptionHandler(NoSuchFieldException.class)
    public String handleIdNotFoundException(){
        return "comment does not exist";
    }


}
