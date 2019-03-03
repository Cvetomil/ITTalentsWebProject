package ittalents.webappsports.dto;

import ittalents.webappsports.models.Comment;
import ittalents.webappsports.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

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
    private List<Comment> comments;

    public UserDTO convertToDTO(User user){
        this.id = user.getId();
        this.roleId = user.getRoleId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.comments = user.getComments();

        return this;
    }

}
