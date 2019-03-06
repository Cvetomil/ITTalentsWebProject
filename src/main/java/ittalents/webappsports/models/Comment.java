package ittalents.webappsports.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long artId;
    private boolean isEdited = false;
    private long userId;
    private String text;
    private LocalDateTime publishTime = LocalDateTime.now();
    private LocalDateTime lastEdited;
    @OneToMany(mappedBy = "commentId", cascade = CascadeType.ALL)
        @OnDelete(action = OnDeleteAction.CASCADE)
   private List<CommentLike> likes;
    @OneToMany(mappedBy = "commentId", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<CommentDislike> dislikes;
}
