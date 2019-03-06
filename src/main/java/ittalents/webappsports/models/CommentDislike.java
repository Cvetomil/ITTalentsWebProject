package ittalents.webappsports.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "dislikes")
@Entity
@IdClass(CommentDislike.CommentDisLikeId.class)
public class CommentDislike {
    @Id
    private long userId;
    @Id
    private long commentId;


    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class CommentDisLikeId implements Serializable {
        private long userId;
        @JsonIgnore
        private long commentId;
    }

}