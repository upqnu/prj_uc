package pp.rsmmm.domain.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pp.rsmmm.domain.team.dto.TeamCreateRequestDto;
import pp.rsmmm.domain.team.dto.TeamCreateResponseDto;
import pp.rsmmm.domain.team.service.TeamService;
import pp.rsmmm.domain.teamsetting.entity.TeamSetting;
import pp.rsmmm.domain.teamsetting.service.TeamSettingService;

@RequiredArgsConstructor
@RequestMapping("/api/teams")
@RestController
@Tag(name = "Team", description = "Team(프로젝트 관리 기본 단위) API")
public class TeamController {

    private final TeamService teamService;
    private final TeamSettingService teamSettingService;

    /**
     * 팀 & 팀구성 생성
     * @param teamCreateRequestDto
     * @return
     */
    @Operation(summary = "팀 생성", description = "프로젝트를 관리할 팀을 생성합니다")
    @Parameter(name = "teamCreateRequestDto", description = "팀 이름, 칸반보드 이름 필수 입력")
    @PostMapping("/create")
    public ResponseEntity<TeamCreateResponseDto> createTeam(@RequestBody TeamCreateRequestDto teamCreateRequestDto) {
        TeamCreateResponseDto teamCreateResponseDto = teamService.createTeam(teamCreateRequestDto);
        return ResponseEntity.status(teamCreateResponseDto.getStatus()).body(teamCreateResponseDto);
    }

    /**
     * 팀 정보 조회
     * @param teamId
     * @return
     */
    @Operation(summary = "팀 조회", description = "팀을 조회성합니다")
    @Parameter(name = "teamId")
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamSetting> getTeam(@PathVariable Long teamId) {
        TeamSetting teamSetting = teamService.getTeam(teamId);
        return ResponseEntity.ok(teamSetting);
    }

    /**
     * 팀원 초대
     * @param teamId
     * @param invitedMemberName
     * @return
     */
    @Operation(summary = "팀에 다른 사용자 초대", description = "팀원이 될 다른 사용자를 초대합니다 (팀을 생성하여 다른 사용자를 초대하는 사용자는 팀장이 됩니다)")
    @Parameter(name = "teamId / invitedMemberName", description = "초대할 사용자의 이름 필수 입력")
    @PostMapping("/{teamId}/invite")
    public ResponseEntity<String> inviteMate(
            @PathVariable Long teamId, @RequestParam String invitedMemberName
    ) {
        teamSettingService.sendInvitation(teamId, invitedMemberName);
        return ResponseEntity.ok(invitedMemberName + "님을 성공적으로 초대하였습니다.");
    }

    /**
     * 팀원 초대에 대해 승낙 또는 거절
     * @param teamId
     * @param inviteeId
     * @param accept
     * @return
     */
    @Operation(summary = "특정 팀으로의 초대에 수락 또는 거절", description = "특정 팀으로 초대를 받은 사용자는 수락 또는 거절할 수 있습니다 (수락한 경우, 팀원이 됩니다)")
    @Parameter(name = "teamId / inviteeId / ", description = "accept = true라면 초대 수락, false라면 초대 거절")
    @PostMapping("/{teamId}/invitation/{inviteeId}")
    public ResponseEntity<String> respondToInvitation(
            @PathVariable Long teamId, @PathVariable Long inviteeId, @RequestParam(name = "accept") boolean accept
    ) {
        teamSettingService.respondToInvitation(teamId, inviteeId, accept);

        String responseMessage = accept ? "팀원 초대를 수락하셨습니다." : "팀원 초대를 거절하셨습니다.";
        return ResponseEntity.ok(responseMessage);
    }
}
