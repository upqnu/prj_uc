package pp.rsmmm.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TeamCreateResponseDto {

    private Integer status;
    private String message;
}
