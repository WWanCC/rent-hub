package renthub.domain.dto;

import lombok.Data;

@Data
public class HouseTagDTO {
    private Integer houseId;
    private Integer tagId;
    private String tagName;
}
