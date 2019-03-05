package ittalents.webappsports.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private String password;
    @Column(unique = true, length = 256)
    @NotNull
    private String email;
    @Min(1)
    private int age;
    private enum Gender{
        MALE,FEMALE
    }

    @OneToMany(mappedBy = "userId")
    private List<Comment> comments;

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
