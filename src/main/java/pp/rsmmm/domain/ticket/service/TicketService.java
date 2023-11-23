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
import pp.rsmmm.domain.ticket.dto.TicketResponseDto;
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
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        Progress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new EntityNotFoundException("진행상황을 찾을 수 없습니다."));

        // 현재 로그인한 사용자가 progress가 속한 팀의 팀장 또는 팀원인지 확인
        String memberName = tokenProvider.getMemberNameFromToken();
        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new EntityNotFoundException("팀장 또는 팀원을 찾을 수 없습니다."));

        List<TeamSetting> teamSettings = teamSettingRepository.findByTeam(team);
        for (TeamSetting teamSetting : teamSettings) {
            if (teamSetting.getMember() != member) {
                continue;
            }

            if (teamSetting.getInviteStatus() == InviteStatus.RECEIVED || teamSetting.getInviteStatus() == InviteStatus.REFUSED) {
                throw new IllegalArgumentException("진행상황의 순서를 변경할 수 있는 권한이 없습니다.");
            }
        }

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

    @Transactional
    public void deleteTicket(Long teamId, Long progressId, Long ticketId) {
        // 해당 팀, 진행상황, 티켓이 존재하는지 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        Progress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new EntityNotFoundException("진행상황을 찾을 수 없습니다."));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("티켓을 찾을 수 없습니다."));

        // 현재 로그인한 사용자가 progress가 속한 팀의 팀장 또는 팀원인지 확인
        String memberName = tokenProvider.getMemberNameFromToken();
        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new EntityNotFoundException("팀장 또는 팀원을 찾을 수 없습니다."));

        List<TeamSetting> teamSettings = teamSettingRepository.findByTeam(team);
        for (TeamSetting teamSetting : teamSettings) {
            if (teamSetting.getMember() != member) {
                continue;
            }

            if (teamSetting.getInviteStatus() == InviteStatus.RECEIVED || teamSetting.getInviteStatus() == InviteStatus.REFUSED) {
                throw new IllegalArgumentException("진행상황을 확인할 수 있는 권한이 없습니다.");
            }
        }

        // 티켓 삭제
        ticketRepository.deleteById(ticketId);
    }
}
