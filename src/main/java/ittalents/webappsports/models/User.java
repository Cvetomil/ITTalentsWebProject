package ittalents.webappsports.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long roleId = 2;
    @Column(unique = true, length = 256)
    private String username;
    @JsonIgnore
    private String password;
    @Column(unique = true, length = 256)
    private String email;

    @Override
    public String toString() {
        return "User{" +
                "roleId=" + roleId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
