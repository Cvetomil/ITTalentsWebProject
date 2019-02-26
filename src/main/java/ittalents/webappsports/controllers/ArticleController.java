package ittalents.webappsports.controllers;

import ittalents.webappsports.models.Article;
import ittalents.webappsports.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController extends SportalController{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ArticleRepository ar;

    @PostMapping("/add")
    public Long addArticle(@RequestBody Article article){
      ar.save(article);

       return article.getId();
    }
    @GetMapping("/getAll")
    public List<Article> getAllArticles(){

        return ar.findAll();
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable long id ) throws Exception{

        if (ar.existsById(id)) {
            return ar.findById(id).get();
        }
throw new NoSuchFieldException();
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler (){
        return "Hendulnahme exceptiona";
    }

}
