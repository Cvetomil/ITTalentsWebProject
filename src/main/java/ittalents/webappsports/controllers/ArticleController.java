package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.NotFoundException;
import ittalents.webappsports.models.Article;
import ittalents.webappsports.models.Category;
import ittalents.webappsports.repositories.ArticleRepository;
import ittalents.webappsports.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ArticleController extends SportalController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ArticleRepository ar;
    @Autowired
    CategoryRepository cr;

   @PostMapping("/users/addArticle")
    //TODO validate user and article content
    public void addArticle(@RequestBody Article article) {
        ar.save(article);
    }


    @GetMapping("/articles/{id}")
    public Article getArticleById(@PathVariable long id) throws NotFoundException {
        Optional<Article> a = ar.findById(id);
               if (!a.isPresent()){
           throw new NotFoundException("The article does not exist!");
        } else {
            Article article = a.get();
            article.setReadCount(article.getReadCount() + 1);
            article.setDayReads(article.getDayReads() + 1);
            ar.save(article);
            return article;
        }
            }

    @GetMapping("/search/{title}")
    public Collection<Article> findArticlesByNameOrCategory (@PathVariable String title){

       List<Article> fromArticles =  ar.findAllByTitleContaining(title);
       List<Category> fromCategories = cr.getAllByNameContaining(title);

       Set<Article> foundArticles = new HashSet<>();
       foundArticles.addAll(fromArticles);
       fromCategories.forEach(category -> {
           foundArticles.addAll(category.getArticles());
       });

       return foundArticles;
    }

}
