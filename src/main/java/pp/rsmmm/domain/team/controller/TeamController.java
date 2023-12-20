package pp.rsmmm.domain.team.controller;

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
public class TeamController {

    private final TeamService teamService;
    private final TeamSettingService teamSettingService;

    /**
     * 팀 & 팀구성 생성
     * @param teamCreateRequestDto
     * @return
     */
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
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamSetting> getTeam(@PathVariable Long teamId) {
        TeamSetting teamSetting = teamService.getTeam(teamId);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok(teamSetting);
    }

    /**
     * 팀원 초대
     * @param teamId
     * @param invitedMemberName
     * @return
     */
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
    @PostMapping("/{teamId}/invitation/{inviteeId}")
    public ResponseEntity<String> respondToInvitation(
            @PathVariable Long teamId, @PathVariable Long inviteeId, @RequestParam(name = "accept") boolean accept
    ) {
        teamSettingService.respondToInvitation(teamId, inviteeId, accept);

        String responseMessage = accept ? "팀원 초대를 수락하셨습니다." : "팀원 초대를 거절하셨습니다.";
        return ResponseEntity.ok(responseMessage);
    }
}
