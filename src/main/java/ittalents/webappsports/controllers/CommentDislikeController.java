package ittalents.webappsports.controllers;

import ittalents.webappsports.repositories.CommentDislikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentDislikeController {

    @Autowired
    CommentDislikeRepository cdlr;
}
