package pp.rsmmm.domain.ticket.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TicketResponseDto {

    private Integer status;
    private String message;
}
