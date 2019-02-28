package ittalents.webappsports.repositories;

import ittalents.webappsports.models.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
