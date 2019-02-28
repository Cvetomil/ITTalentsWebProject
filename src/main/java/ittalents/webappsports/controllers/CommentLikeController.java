package ittalents.webappsports.controllers;

import ittalents.webappsports.repositories.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentLikeController {

    @Autowired
    CommentLikeRepository clr;
}
