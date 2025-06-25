package renthub.po;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Restult {
    private String code;

    private String message;

    public static Restult success(Object data) {
        return new Restult("200", "success");
    }
}
