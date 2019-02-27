package ittalents.webappsports.repositories;

import ittalents.webappsports.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

List<Article> getAllByCatId (long id);

}
