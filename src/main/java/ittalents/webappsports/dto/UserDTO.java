package ittalents.webappsports.dto;

import ittalents.webappsports.models.Comment;
import ittalents.webappsports.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Component
public class UserDTO {
    private long id;
    private long roleId;
    private String username;
    private String email;
    private int age;
    private List<Comment> comments;
    private boolean isConfirmed;

    public UserDTO convertToDTO(User user){
        this.id = user.getId();
        this.roleId = user.getRoleId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.comments = user.getComments();
        this.age = user.getAge();
        this.isConfirmed = user.isConfirmed();
        return this;
    }
}
