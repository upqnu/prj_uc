package pp.rsmmm.domain.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketCreateRequestDto {

    private String title;

    private String tag;

    private Double personHour;

    private LocalDateTime dueDate;
}
