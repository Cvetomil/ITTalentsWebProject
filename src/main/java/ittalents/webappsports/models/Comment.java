package ittalents.webappsports.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private long userId;
    private String text;
    private LocalDateTime publish_time = LocalDateTime.now();
  @OneToMany
  @JoinColumn(name = "commentId")
  private List<CommentLike> likes;
    @OneToMany
    @JoinColumn(name = "commentId")
       private List<CommentDislike> dislikes;


}
