package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.BadRequestException;
import ittalents.webappsports.exceptions.NotAdminException;
import ittalents.webappsports.exceptions.NotFoundException;
import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.Article;
import ittalents.webappsports.models.Category;
import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.ArticleRepository;
import ittalents.webappsports.repositories.CategoryRepository;
import ittalents.webappsports.util.userAuthorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class ArticleController extends SportalController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ArticleRepository ar;
    @Autowired
    CategoryRepository cr;

    //Request article
    @GetMapping("/articles/{id}")
    public Article getArticleById(@PathVariable long id) throws NotFoundException {
        Optional<Article> a = ar.findById(id);
        if (!a.isPresent()) {
            throw new NotFoundException("The article does not exist!");
        } else {
            Article article = a.get();
            article.setReadCount(article.getReadCount() + 1);
            article.setDayReads(article.getDayReads() + 1);
            ar.save(article);
            return article;
        }
    }

    //Add new article
    @PostMapping("/users/addArticle")
    public void addArticle(@RequestBody Article article, HttpSession session)
            throws NotAdminException, UserNotLoggedException, BadRequestException {

        userAuthorities.validateAdmin(session);
        if (!cr.findById(article.getCatId()).isPresent()) {
            throw new BadRequestException("The category does not exist!");
        }
        if (ar.findByTitle(article.getTitle()) != null) {
            throw new BadRequestException("An article with this title already exists!");
        }
        User user = (User)session.getAttribute("Logged");
        article.setAuthor(user.getUsername());
        ar.save(article);
    }

    @GetMapping("/search/{title}")
    public Collection<Article> findArticlesByNameOrCategory(@PathVariable String title) {

        List<Article> fromArticles = ar.findAllByTitleContaining(title);
        List<Category> fromCategories = cr.getAllByNameContaining(title);

        Set<Article> foundArticles = new HashSet<>();
        foundArticles.addAll(fromArticles);
        fromCategories.forEach(category -> {
            foundArticles.addAll(category.getArticles());
        });

        return foundArticles;
    }

    @GetMapping("/articles/top5")
    public Collection<Article> find5MostReadArticlesForTheDay() {
        List<Article> list = new ArrayList<>();
        String sql = "SELECT id FROM articles ORDER BY day_reads DESC LIMIT 5";
        List<Long> longId = jdbcTemplate.queryForList(sql, Long.class);
        longId.forEach(id -> {
            Article article = ar.findById(id).get();
            list.add(article);
        });
        // List<Article> list = ar.findAll();
        // list.sort(Comparator.comparing(Article::getDayReads).reversed());
        return list;
    }

    @Scheduled(cron = "* 59 23 * * *")
    public void resetDayReads() {
        jdbcTemplate.execute("UPDATE articles SET day_reads = 0");
        System.out.println(LocalDateTime.now());
    }
}
