package ittalents.webappsports.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    private long id;
    private String name;
//@OneToMany(mappedBy = "catId", targetEntity = Article.class,
//    fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "catId")
    private List<Article> articles;
}
