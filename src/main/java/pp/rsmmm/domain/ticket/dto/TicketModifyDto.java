package pp.rsmmm.domain.ticket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TicketModifyDto {

    private String title;
    private String tag;
    private Double personHour;
    private LocalDateTime dueDate;
    private Long memberId;
}
