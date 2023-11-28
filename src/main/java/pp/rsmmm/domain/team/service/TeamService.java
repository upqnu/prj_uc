package pp.rsmmm.domain.team.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.rsmmm.domain.member.entity.Member;
import pp.rsmmm.domain.member.repository.MemberRepository;
import pp.rsmmm.domain.team.dto.TeamCreateRequestDto;
import pp.rsmmm.domain.team.dto.TeamCreateResponseDto;
import pp.rsmmm.domain.team.entity.Team;
import pp.rsmmm.domain.team.repository.TeamRepository;
import pp.rsmmm.domain.teamsetting.entity.InviteStatus;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.teamsetting.repository.TeamSettingRepository;
import pp.rsmmm.domain.teamsetting.service.TeamSettingService;
import pp.rsmmm.global.config.jwt.TokenProvider;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TokenProvider tokenProvider;
    private final TeamSettingService teamSettingService;
    private final MemberRepository memberRepository;
    private final TeamSettingRepository teamSettingRepository;

    /**
     * 팀 생성 로직
     * @param teamCreateRequestDto
     * @return
     */
    @Transactional
    public TeamCreateResponseDto createTeam(TeamCreateRequestDto teamCreateRequestDto) {

        // 팀명 중복 체크
        if (teamRepository.existsByName(teamCreateRequestDto.getName())) {
            return TeamCreateResponseDto.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("이미 존재하는 팀명입니다. 다른 팀명을 입력해주세요.")
                    .build();
        }

        // 팀 생성 및 저장
        Team team = Team.builder()
                .name(teamCreateRequestDto.getName())
                .kanban(teamCreateRequestDto.getKanban())
                .build();

        teamRepository.save(team);

        // 팀 구성(TeamSetting) 생성 및 저장
        String memberName = tokenProvider.getMemberNameFromToken();

        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new EntityNotFoundException("팀을 생성한 사용자를 칮을 수 없습니다."));

        teamSettingService.createTeamSetting(member, team);

        // 팀 생성 통보
        return TeamCreateResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message("팀 " + team.getName() + "이 성공적으로 생성되었습니다.")
                .build();
    }

    /**
     * 팀 정보 조회 로직
     * @param teamId
     * @return
     */
    @Transactional
    public TeamSetting getTeam(Long teamId) {

        // 해당 team이 존재하는지 확인
        TeamSetting existsTeam = teamSettingRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        // 해당 team의 팀장 또는 팀원만 팀구성(teamSetting) 확인이 가능
        String memberName = tokenProvider.getMemberNameFromToken();
        Member teamMember = memberRepository.findByName(memberName)
                .orElseThrow(() -> new EntityNotFoundException(memberName + "님의 정보가 존재하지 않습니다."));

        List<TeamSetting> teamSettingsOfMember = null;
        try {
            teamSettingsOfMember = teamSettingRepository.findByMember(teamMember);
        } catch (EntityNotFoundException e) {
            System.out.println("팀 구성 정보를 찾을 수 없습니다.");
        }


        // 팀장 및 (팀장의 초대를 수락한) 팀원에게만 팀 조회 권한 부여
        for (TeamSetting teamSetting : teamSettingsOfMember) {
            if (!teamSetting.equals(existsTeam)) {
                continue;
            }

            if (teamSetting.getInviteStatus() == InviteStatus.RECEIVED
                    || teamSetting.getInviteStatus() == InviteStatus.REFUSED ) {
                throw new EntityNotFoundException("팀을 조회할 권한이 없습니다.");
            }

            return teamSetting;
        }

        return null;
    }

}
