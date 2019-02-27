package ittalents.webappsports.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "articles")

public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        private long catId;
    private String title;
    private String text;
    private int readCount;
    private int dayReads;
    @OneToMany(mappedBy = "artId")
    private List<Comment> comments;


}
