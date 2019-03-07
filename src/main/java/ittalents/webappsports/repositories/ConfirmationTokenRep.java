package ittalents.webappsports.repositories;

import ittalents.webappsports.models.ConfirmationToken;
import ittalents.webappsports.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ConfirmationTokenRep extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByCode(String code);
}
