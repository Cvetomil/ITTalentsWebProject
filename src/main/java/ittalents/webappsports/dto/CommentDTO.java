package ittalents.webappsports.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@Component
public class CommentDTO {

    private String text;
}
