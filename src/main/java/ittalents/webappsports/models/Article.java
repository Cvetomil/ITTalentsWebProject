package ittalents.webappsports.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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
    private LocalDate dateAdded = LocalDate.now();
    private int readCount;
    private int dayReads;
    @OneToMany(mappedBy = "artId")
    private List<Comment> comments;
    @OneToMany(mappedBy = "artId", cascade = CascadeType.ALL)
private List<Picture> pictures;

}
