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

    @PostMapping("/addArticle")
    public void addArticle(@RequestBody Article article){
      ar.save(article);
          }

    @GetMapping("/getAll")
    public List<Article> getAllArticles(){

        return ar.findAll();
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable long id ) {

        Article a = ar.findById(id).get();
        a.setReadCount(a.getReadCount() +1);
        ar.save(a);
                return a;
           }

    @GetMapping("/category/{id}")
    public List<Article> getArticlesByCatId(@PathVariable long id ) {
        return ar.getAllByCatId(id);
    }



}
