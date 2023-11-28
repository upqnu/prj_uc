package pp.rsmmm.domain.ticket.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.progress.repository.ProgressRepository;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.team.repository.TeamRepository;
import pp.rsmmm.domain.teamsetting.entity.InviteStatus;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.teamsetting.repository.TeamSettingRepository;
import pp.rsmmm.domain.ticket.dto.TicketCreateRequestDto;
import pp.rsmmm.domain.ticket.dto.TicketOrderModifyDto;
import pp.rsmmm.domain.ticket.dto.TicketResponseDto;
import pp.rsmmm.domain.ticket.dto.TicketModifyDto;
import pp.rsmmm.domain.ticket.entity.Ticket;
import pp.rsmmm.domain.ticket.repository.TicketRepository;
import pp.rsmmm.global.config.jwt.TokenProvider;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TeamRepository teamRepository;
    private final TeamSettingRepository teamSettingRepository;
    private final ProgressRepository progressRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    /**
     * Ticket 생성 로직
     * @param teamId
     * @param progressId
     * @param ticketCreateRequestDto
     * @return
     */
    @Transactional
    public TicketResponseDto createTicket(Long teamId, Long progressId, TicketCreateRequestDto ticketCreateRequestDto) {
        // 해당 팀, 진행상황이 존재하는지 확인
        existsTeam(teamId);
        existsProgress(progressId);

        Progress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new EntityNotFoundException("진행상황을 찾을 수 없습니다."));

        // 현재 로그인한 사용자가 progress가 속한 팀의 팀장 또는 팀원이라면 member 반환
        Member member = getTeamLeaderOrMate(teamId);

        // Ticket 생성
        int numOfTickets = ticketRepository.countByProgress(progress) + 1;
        Ticket ticket = Ticket.builder()
                .title(ticketCreateRequestDto.getTitle())
                .numbering(numOfTickets)
                .tag(ticketCreateRequestDto.getTag())
                .personHour(ticketCreateRequestDto.getPersonHour())
                .dueDate(ticketCreateRequestDto.getDueDate())
                .progress(progress)
                .memberId(member.getId())
                .build();

        ticketRepository.save(ticket);

        // Ticket 생성 통보
        TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message("<" + ticket.getTitle() + "> 티켓이 생성되었습니다.")
                .build();

        return ticketResponseDto;
    }

    /**
     * Ticket 삭제 로직
     * @param teamId
     * @param progressId
     * @param ticketId
     */
    @Transactional
    public void deleteTicket(Long teamId, Long progressId, Long ticketId) {
        // 해당 팀, 진행상황, 티켓이 존재하는지 확인
        existsTeam(teamId);
        existsProgress(progressId);
        existsTicket(ticketId);

        // 현재 로그인한 사용자가 progress가 속한 팀의 팀장 또는 팀원인지 확인
        isTeamLeaderOrMate(teamId);

        // 티켓 삭제
        ticketRepository.deleteById(ticketId);
    }

    /**
     * Ticket 수정 로직
     * @param ticketModifyDto
     * @param teamId
     * @param progressId
     * @param ticketId
     * @return
     */
    @Transactional
    public Ticket modifyTicket(TicketModifyDto ticketModifyDto, Long teamId, Long progressId, Long ticketId) {
        // 해당 팀, 진행상황, 티켓이 존재하는지 확인
        existsTeam(teamId);
        existsProgress(progressId);
        existsTicket(ticketId);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("티켓에 존재하지 않거나 찾을 수 없습니다."));

        // 현재 로그인한 사용자가 progress가 속한 팀의 팀장 또는 팀원인지 확인
        isTeamLeaderOrMate(teamId);

        // 티켓 수정
        ticket.modifyTicket(
                ticketModifyDto.getTitle(),
                ticketModifyDto.getTag(),
                ticketModifyDto.getPersonHour(),
                ticketModifyDto.getDueDate(),
                ticketModifyDto.getMemberId()
        );
        ticketRepository.save(ticket);
        return ticket;
    }

    /**
     * Ticket 순서 수정 로직
     * @param ticketOrderModifyDto
     * @param teamId
     * @param progressId
     * @param ticketId
     * @return
     */
    @Transactional
    public Ticket modifyTicketOrder(TicketOrderModifyDto ticketOrderModifyDto, Long teamId, Long progressId, Long ticketId) {
        // 해당 팀, 진행상황, 티켓이 존재하는지 확인
        existsTeam(teamId);
        existsProgress(progressId);
        existsTicket(ticketId);

        Progress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new EntityNotFoundException("진행상황을 찾을 수 없습니다."));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("티켓에 존재하지 않거나 찾을 수 없습니다."));

        // 현재 로그인한 사용자가 progress가 속한 팀의 팀장 또는 팀원인지 확인
        isTeamLeaderOrMate(teamId);

        // 진행상황 순서 변경
        if (ticketOrderModifyDto.getProgressNum().equals(progress.getNumbering())) {
            modifyTicketOrderInProgress(ticket, progress, ticketOrderModifyDto);
        } else {
            modifyTicketOrderBetweenProgresses(ticket, progress, ticketOrderModifyDto);
        }

        return ticket;
    }

    /**
     * 동일 Progress 내에서 Ticket 순서 변경 로직
     * @param ticket
     * @param progress
     * @param ticketOrderModifyDto
     */
    @Transactional
    public void modifyTicketOrderInProgress(Ticket ticket, Progress progress, TicketOrderModifyDto ticketOrderModifyDto) {
        int initNum = ticket.getNumbering();
        int newNum = ticketOrderModifyDto.getTicketNum();

        List<Ticket> ticketList = ticketRepository.findTicketByProgress(progress);
        for (Ticket t : ticketList) {
            int otherNum = t.getNumbering();
            // 바꾸려는 티켓의 순서 변경
            if (t == ticket) {
                ticket.ticketOrderUpdate(newNum);
                ticketRepository.save(t);
            }
            // 같은 팀 내 다른 티켓의 순서 변경
            else {
                if (initNum < newNum && otherNum > initNum && otherNum <= newNum) {
                    t.ticketOrderUpdate(otherNum - 1);
                    ticketRepository.save(t);
                }
                else if (initNum > newNum && otherNum < initNum && otherNum >= newNum) {
                    t.ticketOrderUpdate(otherNum + 1);
                    ticketRepository.save(t);
                }
            }
        }
    }

    /**
     * Progress 옮기면서 Ticket 순서 변경 로직
     * @param ticket
     * @param progress
     * @param ticketOrderModifyDto
     */
    @Transactional
    public void modifyTicketOrderBetweenProgresses(Ticket ticket, Progress progress, TicketOrderModifyDto ticketOrderModifyDto) {
        int initProgressNum = progress.getNumbering();
        int targetProgressNum = ticketOrderModifyDto.getProgressNum();

        int initTicketNum = ticket.getNumbering();
        int newTicketNum = ticketOrderModifyDto.getTicketNum();

        Progress targetProgress = progressRepository.findProgressByNumbering(targetProgressNum)
                .orElseThrow(() -> new EntityNotFoundException("티켓을 옮기려는 진행상황이 존재하지 않습니다."));

        // 티켓이 옮겨가려는 progress에서 티켓의 순서 변경
        List<Ticket> newTicketList = ticketRepository.findTicketByProgress(targetProgress);
        for (Ticket newT : newTicketList) {
            int otherNum = newT.getNumbering();
            // 티켓이 옮겨가려는 progress에서 - 옮겨가려는 티켓의 numbering보다 더 크거나 같은 numbering을 가진 ticket은 numbering +1 해준다
            if (otherNum >= newTicketNum) {
                newT.ticketOrderUpdate(otherNum + 1);
                ticketRepository.save(newT);
            }
        }

        // 티켓이 옮겨가려는 progress로 티켓을 옮기는 것은 새로운 티켓을 생성하는 것과 같음
        Ticket newTicket = Ticket.builder()
                .title(ticket.getTitle())
                .numbering(newTicketNum)
                .tag(ticket.getTag())
                .personHour(ticket.getPersonHour())
                .dueDate(ticket.getDueDate())
                .memberId(ticket.getMemberId())
                .progress(targetProgress)
                .build();

        ticketRepository.save(newTicket);

        // 티켓이 원래 있던 progress에서 티켓의 순서 변경
        List<Ticket> initTicketList = ticketRepository.findTicketByProgress(progress);
        for (Ticket initT : initTicketList) {
            int otherNum = initT.getNumbering();
            // 옮기려는 티켓이 원래 있던 progress에서 - 옮기려는 티켓의 numbering보다 더 큰 numbering을 가진 ticket은 numbering -1 해준다
            if (otherNum > initTicketNum) {
                initT.ticketOrderUpdate(otherNum - 1);
                ticketRepository.save(initT);
            }
            // 옮기려는 티켓이 원래 있던 progress에서 - 옮기려는 티켓을 삭제
            else if (initT == ticket) {
                ticketRepository.deleteById(initT.getId());
            }
        }
    }

    /**
     * 팀 존재 여부 확인
     * @param teamId
     */
    private void existsTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않거나 찾을 수 없습니다."));
    }

    /**
     * 진행상황(progress) 존재 여부 확인
     * @param progressId
     */
    private void existsProgress(Long progressId) {
        Progress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new EntityNotFoundException("진행상황이 존재하지 않거나 찾을 수 없습니다."));
    }

    /**
     * 티켓 존재 여부 확인
     * @param ticketId
     */
    private void existsTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("티켓이 존재하지 않거나 찾을 수 없습니다."));
    }

    private void isTeamLeaderOrMate(Long teamId) {
        String memberName = tokenProvider.getMemberNameFromToken();
        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new EntityNotFoundException("팀장 또는 팀원을 찾을 수 없습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        List<TeamSetting> teamSettings = teamSettingRepository.findByTeam(team);
        for (TeamSetting teamSetting : teamSettings) {
            if (teamSetting.getMember() != member) {
                continue;
            }

            if (teamSetting.getInviteStatus() == InviteStatus.RECEIVED || teamSetting.getInviteStatus() == InviteStatus.REFUSED) {
                throw new IllegalArgumentException("티켓을 확인할 수 있는 권한이 없습니다.");
            }
        }
    }

    private Member getTeamLeaderOrMate(Long teamId) {
        String memberName = tokenProvider.getMemberNameFromToken();
        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new EntityNotFoundException("팀장 또는 팀원을 찾을 수 없습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        List<TeamSetting> teamSettings = teamSettingRepository.findByTeam(team);
        for (TeamSetting teamSetting : teamSettings) {
            if (teamSetting.getMember() != member) {
                continue;
            }

            if (teamSetting.getInviteStatus() == InviteStatus.RECEIVED || teamSetting.getInviteStatus() == InviteStatus.REFUSED) {
                throw new IllegalArgumentException("티켓을 확인할 수 있는 권한이 없습니다.");
            }
        }

        return member;
    }
}
