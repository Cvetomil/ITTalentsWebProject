package ittalents.webappsports.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String author;
    private boolean isEdited = false;
    private LocalDateTime lastEdited;
    @OneToMany(mappedBy = "artId", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
        private List<Comment> comments;
    @OneToMany(mappedBy = "artId", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
        private List<Picture> pictures;
    @OneToMany(mappedBy = "artId", cascade = CascadeType.ALL)
    private List<Video> videos;

}
