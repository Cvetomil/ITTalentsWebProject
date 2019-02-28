package ittalents.webappsports.controllers;

import ittalents.webappsports.models.Article;
import ittalents.webappsports.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController extends SportalController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ArticleRepository ar;

    @PostMapping("/users/addArticle")
    //TODO validate user and article content
    public void addArticle(@RequestBody Article article) {
        ar.save(article);
    }


    @GetMapping("/articles/{id}")
    public Article getArticleById(@PathVariable long id) {

        Article a = ar.findById(id).get();
        a.setReadCount(a.getReadCount() + 1);
        a.setDayReads(a.getDayReads() + 1);
        ar.save(a);

        return a;
    }

}
