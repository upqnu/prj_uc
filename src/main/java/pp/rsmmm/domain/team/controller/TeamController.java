package pp.rsmmm.domain.team.controller;

import lombok.RequiredArgsConstructor;
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

    @PostMapping("/create")
    public ResponseEntity<TeamCreateResponseDto> createTeam(@RequestBody TeamCreateRequestDto teamCreateRequestDto) {
        TeamCreateResponseDto teamCreateResponseDto = teamService.createTeam(teamCreateRequestDto);
        return ResponseEntity.status(teamCreateResponseDto.getStatus()).body(teamCreateResponseDto);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamSetting> getTeam(@PathVariable Long teamId) {
        TeamSetting teamSetting = teamSettingService.getTeam(teamId);
        return ResponseEntity.ok(teamSetting);
    }

    @PostMapping("/{teamId}/invite")
    public ResponseEntity<String> inviteMate(
            @PathVariable Long teamId,
            @RequestParam String invitedMemberName) {

        teamSettingService.sendInvitation(teamId, invitedMemberName);
        return ResponseEntity.ok(invitedMemberName + "님을 성공적으로 초대하였습니다.");
    }
}
