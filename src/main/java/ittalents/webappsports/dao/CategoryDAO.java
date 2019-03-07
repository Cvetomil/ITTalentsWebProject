package ittalents.webappsports.dao;

import ittalents.webappsports.dto.CategoryReadsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String CATEGORY_TOTAL_READS =
            "SELECT c.name AS category, SUM(a.read_count) AS totalReads " +
                    "FROM categories c " +
                    "LEFT JOIN articles a ON (c.id = a.cat_id) " +
                    "GROUP BY c.name ORDER BY totalReads DESC;";

    public List<CategoryReadsDTO> getCategoriesByTotalReads() {
        return jdbcTemplate.query(
                CATEGORY_TOTAL_READS,
                new BeanPropertyRowMapper<>(CategoryReadsDTO.class));
    }
}
