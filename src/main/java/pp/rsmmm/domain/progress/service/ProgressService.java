package pp.rsmmm.domain.progress.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.domain.progress.dto.ProgressCreateRequestDto;
import pp.rsmmm.domain.progress.dto.ProgressCreateResponseDto;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.progress.repository.ProgressRepository;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.team.repository.TeamRepository;
import pp.rsmmm.domain.teamsetting.entity.InviteStatus;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.teamsetting.repository.TeamSettingRepository;
import pp.rsmmm.global.config.jwt.TokenProvider;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final TeamSettingRepository teamSettingRepository;
    private final TeamRepository teamRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    /**
     * 진행상황(Progress) 생성 로직
     * @param teamId
     * @param progressCreateRequestDto
     * @return
     */
    @Transactional
    public ProgressCreateResponseDto createProgress(Long teamId, ProgressCreateRequestDto progressCreateRequestDto) {
        // 현재 로그인한 유저가 해당 팀의 팀장인지 여부 확인
        String leaderName = tokenProvider.getMemberNameFromToken();
        Member leaderMember = memberRepository.findByName(leaderName)
                .orElseThrow(() -> new EntityNotFoundException("팀장 여부를 확인할 수 없습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        List<TeamSetting> teamSettings = teamSettingRepository.findByTeam(team);
        for (TeamSetting teamSetting : teamSettings) {
            if (teamSetting.getInviteStatus() != InviteStatus.INVITING) {
                continue;
            }

            if (teamSetting.getMember() != leaderMember) {
                throw new EntityNotFoundException("팀장만이 [진행상황]을 만들 수 있습니다.");
            }
        }

        // Progress 생성
        int numOfProgress = progressRepository.countByTeam(team) + 1; // 이미 생성된 Progress의 숫자 + 1 이 numbering 값이 되어야 함.

        Progress progress = Progress.builder()
                .name(progressCreateRequestDto.getName())
                .numbering(numOfProgress)
                .team(team)
                .build();

        progressRepository.save(progress);

        // Progress 생성 통보
        return ProgressCreateResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message("<" + progress.getName() + "> 진행상황이 생성되었습니다.")
                .build();
    }

    /**
     * 진행상황(progress) 조회 로진
     * @param teamId
     * @param progressId
     * @return
     */
    @Transactional
    public Progress getProgress(Long teamId, Long progressId) {
        // 현재 로그인한 사용자가 progress가 속한 팀의 팀장 또는 팀원인지 확인
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
                throw new IllegalArgumentException("진행상황을 확인할 수 있는 권한이 없습니다.");
            }
        }

        // 조회
        Progress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new EntityNotFoundException("진행상황을 찾을 수 없습니다."));

        return progress;
    }
}
