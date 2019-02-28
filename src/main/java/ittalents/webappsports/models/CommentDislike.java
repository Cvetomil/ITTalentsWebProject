package ittalents.webappsports.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dislikes")
public class CommentDislike {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private long commentId;

}
