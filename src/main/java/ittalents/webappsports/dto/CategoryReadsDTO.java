package ittalents.webappsports.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class CategoryReadsDTO {

    private String category;
    private Long totalReads;

     public void setTotalReads(Long totalReads) {
        if (totalReads == null){
            this.totalReads = 0L;
        } else {
            this.totalReads = totalReads;
        }
    }
}
