package ittalents.webappsports.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class EditedArticleDTO {

    private String title;
    private String text;
}
