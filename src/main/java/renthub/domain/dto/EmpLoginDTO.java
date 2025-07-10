package renthub.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmpLoginDTO {
    @Size(max = 50)
    private String username;
    @Size(max = 100)
    private String password;
}
