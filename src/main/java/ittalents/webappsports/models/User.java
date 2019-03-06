package ittalents.webappsports.models;

import jdk.jfr.BooleanFlag;
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
    private enum Gender{
        MALE,FEMALE
    }
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
    @Column
    private int age;
    @OneToMany(mappedBy = "userId")
    private List<Comment> comments;
    @Column(name = "gender")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", comments=" + comments +
                '}';
    }
}
