package pp.rsmmm.domain.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pp.rsmmm.domain.ticket.dto.TicketCreateRequestDto;
import pp.rsmmm.domain.ticket.dto.TicketOrderModifyDto;
import pp.rsmmm.domain.ticket.dto.TicketResponseDto;
import pp.rsmmm.domain.ticket.dto.TicketModifyDto;
import pp.rsmmm.domain.ticket.entity.Ticket;
import pp.rsmmm.domain.ticket.service.TicketService;

@RequiredArgsConstructor
@RequestMapping(("/api/teams/{teamId}/progresses/{progressId}/tickets"))
@RestController
public class TicketController {

    private final TicketService ticketService;

    /**
     * Ticket 생성
     * @param teamId
     * @param progressId
     * @param ticketCreateRequestDto
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<TicketResponseDto> createTicket(
            @PathVariable Long teamId, @PathVariable Long progressId,
            @RequestBody TicketCreateRequestDto ticketCreateRequestDto
    ) {
        TicketResponseDto ticketResponseDto = ticketService.createTicket(teamId, progressId, ticketCreateRequestDto);
        return ResponseEntity.status(ticketResponseDto.getStatus()).body(ticketResponseDto);
    }

    /**
     * Ticket 삭제
     * @param teamId
     * @param progressId
     * @param ticketId
     * @return
     */
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<String> deleteTicket(
            @PathVariable Long teamId, @PathVariable Long progressId, @PathVariable Long ticketId
    ) {
        ticketService.deleteTicket(teamId, progressId, ticketId);
        return ResponseEntity.ok("티켓 삭제가 완료되었습니다.");
    }

    /**
     * Ticket 수정
     * @param ticketModifyDto
     * @param teamId
     * @param progressId
     * @param ticketId
     * @return
     */
    @PutMapping("{ticketId}")
    public ResponseEntity<Ticket> modifyTicket(
            @RequestBody TicketModifyDto ticketModifyDto,
            @PathVariable Long teamId, @PathVariable Long progressId, @PathVariable Long ticketId
    ) {
        Ticket ticket = ticketService.modifyTicket(ticketModifyDto, teamId, progressId, ticketId);
        return ResponseEntity.ok(ticket);
    }

    /**
     * Ticket 순서 수정
     * @param ticketOrderModifyDto
     * @param teamId
     * @param progressId
     * @param ticketId
     * @return
     */
    @PatchMapping("{ticketId}")
    public ResponseEntity<Ticket> modifyTicketOrder(
            @RequestBody TicketOrderModifyDto ticketOrderModifyDto,
            @PathVariable Long teamId, @PathVariable Long progressId, @PathVariable Long ticketId
    ) {
        Ticket ticket = ticketService.modifyTicketOrder(ticketOrderModifyDto, teamId, progressId, ticketId);
        return ResponseEntity.ok(ticket);
    }
}
