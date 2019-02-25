package ittalents.webappsports.controllers;

import ittalents.webappsports.models.Comment;
import ittalents.webappsports.repositories.ArticleRepository;
import ittalents.webappsports.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/comment/{id}")
    public Comment getCommentById(@PathVariable long id) throws NoSuchFieldException{
        if(commentRepository.existsById(id)) {
            return commentRepository.findById(id).get();
        }
        throw new NoSuchFieldException();
    }

    @GetMapping("/comment/all")
    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }
    @PostMapping("/comment/add")
    public String addComment(@RequestBody Comment comment){
        commentRepository.save(comment);
        return "comment saved";
    }

    @ExceptionHandler(NoSuchFieldException.class)
    public String handleIdNotFoundException(){
        return "comment does not exist";
    }


}
