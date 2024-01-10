package pp.rsmmm.domain.ticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Ticket", description = "Ticket(특정 팀-진행상황 내에서 관리되는 작업) API")
public class TicketController {

    private final TicketService ticketService;

    /**
     * Ticket 생성
     * @param teamId
     * @param progressId
     * @param ticketCreateRequestDto
     * @return
     */
    @Operation(summary = "티켓 생성", description = "티켓을 생성합니다")
    @Parameter(name = "teamId / progressId / ticketCreateRequestDto", description = "티켓 생성 시, title/tag/작업공수(personHour)/완료기한(dueDate) 입력")
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
    @Operation(summary = "티켓 삭제", description = "티켓을 삭제합니다")
    @Parameter(name = "teamId / progressId / ticketId")
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
    @Operation(summary = "티켓 정보 변경", description = "티켓의 정보를 변경합니다")
    @Parameter(name = "ticketModifyDto / teamId / progressId / ticketId", description = "티켓의 title/tag/작업공수(personHour)/완료기한(dueDate) 변경 및 해당 작업(ticket)의 담당 팀원 변경")
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
    @Operation(summary = "티켓의 순서 변경", description = "특정 진행상황 내 티켓의 순서를 변경하거나 티켓을 다른 진행상황으로 옮깁니다")
    @Parameter(name = "ticketOrderModifyDto / teamId / progressId", description = "티켓의 새로운 순서, 티켓이 옮겨갈 진행상 입력")
    @PatchMapping("{ticketId}")
    public ResponseEntity<Ticket> modifyTicketOrder(
            @RequestBody TicketOrderModifyDto ticketOrderModifyDto,
            @PathVariable Long teamId, @PathVariable Long progressId, @PathVariable Long ticketId
    ) {
        Ticket ticket = ticketService.modifyTicketOrder(ticketOrderModifyDto, teamId, progressId, ticketId);
        return ResponseEntity.ok(ticket);
    }
}
