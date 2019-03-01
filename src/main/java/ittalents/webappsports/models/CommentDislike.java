package ittalents.webappsports.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "dislikes")
@Entity
public class CommentDislike {

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    static public class CommentDislikeId implements Serializable{

        private long userId;
        private long commentId;

    }

    @EmbeddedId
    private CommentDislikeId id;

}