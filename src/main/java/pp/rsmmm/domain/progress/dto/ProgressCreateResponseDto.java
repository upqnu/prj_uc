package pp.rsmmm.domain.progress.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProgressCreateResponseDto {

    private Integer status;
    private String message;
}
