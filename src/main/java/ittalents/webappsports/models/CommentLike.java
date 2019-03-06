package ittalents.webappsports.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "likes")
@Entity
@IdClass(CommentLike.CommentLikeId.class)
public class CommentLike {
    @Id
    private long userId;
@Id
    private long commentId;


    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class CommentLikeId implements Serializable {
        private long userId;
        @JsonIgnore
        private long commentId;
    }

}
