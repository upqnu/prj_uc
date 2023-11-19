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
import pp.rsmmm.domain.teamsetting.service.TeamSettingService;
import pp.rsmmm.global.config.jwt.TokenProvider;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TokenProvider tokenProvider;
    private final TeamSettingService teamSettingService;
    private final MemberRepository memberRepository;

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



}
