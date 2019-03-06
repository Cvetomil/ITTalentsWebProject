package ittalents.webappsports.controllers;

import ittalents.webappsports.dao.ArticleDAO;
import ittalents.webappsports.dto.EditedArticleDTO;
import ittalents.webappsports.exceptions.BadRequestException;
import ittalents.webappsports.exceptions.NotAdminException;
import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.Article;
import ittalents.webappsports.models.Category;
import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.ArticleRepository;
import ittalents.webappsports.repositories.CategoryRepository;
import ittalents.webappsports.util.userAuthorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class ArticleController extends SportalController {

    @Autowired
    ArticleDAO articleDAO;
    @Autowired
    ArticleRepository ar;
    @Autowired
    CategoryRepository cr;

    //Request article
    @GetMapping("/articles/{articleId}")
    public Article getArticleById(@PathVariable long articleId) throws BadRequestException {
        checkArticlePresence(articleId);
        Article article = ar.findById(articleId).get();
        article.setReadCount(article.getReadCount() + 1);
        article.setDayReads(article.getDayReads() + 1);
        ar.save(article);
        return article;
    }

    //Add new article
    @PostMapping("/users/article")
    public void addArticle(@RequestBody Article article, HttpSession session)
            throws NotAdminException, UserNotLoggedException, BadRequestException {
        userAuthorities.validateAdmin(session);
        if (!cr.findById(article.getCatId()).isPresent()) {
            throw new BadRequestException("The category does not exist!");
        }
        if (ar.findByTitle(article.getTitle()) != null) {
            throw new BadRequestException("An article with this title already exists!");
        }
        User admin = getUser(session);
        article.setAuthor(admin.getUsername());
        ar.save(article);
    }

    //Edit existing article
    @PutMapping("/users/articles/{articleId}")
    public Article editArticle(@RequestBody EditedArticleDTO articleDTO, HttpSession session, @PathVariable long articleId)
            throws NotAdminException, UserNotLoggedException, BadRequestException {
        userAuthorities.validateAdmin(session);
        checkArticlePresence(articleId);
        validateArticleAuthor(session, articleId);
        Article articleToBeEdited = ar.getOne(articleId);
        if (ar.findByTitle(articleDTO.getTitle()) != null) {
            throw new BadRequestException("Article with this title already exists!");
        }
        articleToBeEdited.setTitle(articleDTO.getTitle());
        articleToBeEdited.setText(articleDTO.getText());
        articleToBeEdited.setEdited(true);
        articleToBeEdited.setLastEdited(LocalDateTime.now());
        ar.save(articleToBeEdited);
        return articleToBeEdited;
    }

    //Delete article
    @DeleteMapping("/users/articles/{articleId}")
    public void deleteArticle(HttpSession session, @PathVariable long articleId)
            throws NotAdminException, UserNotLoggedException, BadRequestException {
        userAuthorities.validateAdmin(session);
        checkArticlePresence(articleId);
        validateArticleAuthor(session, articleId);
        ar.delete(ar.findById(articleId).get());
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
    public Collection<Article> top5ArticlesForTheDay() {
        return articleDAO.find5MostReadArticlesForTheDay();
    }

    @Scheduled(cron = "* 59 23 * * *")
    public void resetCounterForDayReads() {
        articleDAO.resetDayReads();
    }

}
