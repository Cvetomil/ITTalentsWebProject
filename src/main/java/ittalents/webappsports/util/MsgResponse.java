package ittalents.webappsports.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class MsgResponse {
    private int statusCode;
    private String msg;
    private LocalDateTime localDateTime = LocalDateTime.now();

    public MsgResponse(int statusCode, String msg){
        this.statusCode = statusCode;
        this.msg = msg;
    }

}
