package ittalents.webappsports.repositories;

import ittalents.webappsports.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User getByUsername(String username);
    User getByEmail(String email);
}
