package pp.rsmmm.domain.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pp.rsmmm.domain.ticket.dto.TicketCreateRequestDto;
import pp.rsmmm.domain.ticket.dto.TicketResponseDto;
import pp.rsmmm.domain.ticket.service.TicketService;

@RequiredArgsConstructor
@RequestMapping(("/api/teams/{teamId}/progresses/{progressId}/tickets"))
@RestController
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/create")
    public ResponseEntity<TicketResponseDto> createTicket(
            @PathVariable Long teamId, @PathVariable Long progressId,
            @RequestBody TicketCreateRequestDto ticketCreateRequestDto
    ) {
        TicketResponseDto ticketResponseDto = ticketService.createTicket(teamId, progressId, ticketCreateRequestDto);
        return ResponseEntity.status(ticketResponseDto.getStatus()).body(ticketResponseDto);
    }
}
