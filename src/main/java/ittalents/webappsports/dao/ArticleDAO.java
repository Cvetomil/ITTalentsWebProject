package ittalents.webappsports.dao;

import ittalents.webappsports.models.Article;
import ittalents.webappsports.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ArticleDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    ArticleRepository ar;

    private static final String FIND_TOP_5_FOR_THE_DAY = "SELECT id " +
            "FROM articles ORDER BY day_reads DESC LIMIT 5";
    private static final String RESET_DAY_READS = "UPDATE articles SET day_reads = 0";

    public Collection<Article> find5MostReadArticlesForTheDay() {
        List<Article> list = new ArrayList<>();
        List<Long> longId = jdbcTemplate.queryForList(FIND_TOP_5_FOR_THE_DAY, Long.class);
        longId.forEach(id -> {
            Article article = ar.findById(id).get();
            list.add(article);
        });
        return list;
    }

    public void resetDayReads() {
        jdbcTemplate.execute(RESET_DAY_READS);
    }
}
