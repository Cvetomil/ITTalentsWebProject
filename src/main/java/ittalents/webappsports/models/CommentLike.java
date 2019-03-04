package ittalents.webappsports.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@NoArgsConstructor
@EqualsAndHashCode
    static public class CommentLikeId implements Serializable{

        private long userId;
        @JsonIgnore
                private long commentId;

    }

@EmbeddedId
    private CommentLikeId id;

}
