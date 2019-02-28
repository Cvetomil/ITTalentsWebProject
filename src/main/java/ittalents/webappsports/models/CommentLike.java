package ittalents.webappsports.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "likes")
@Entity
public class CommentLike {

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
    static class CommentLikeId implements Serializable{

    @Column(name = "user_id")
    private long userId;
    @Column(name = "comment_id")
    private long commentId;

    }

@EmbeddedId
    private CommentLikeId id;

}
