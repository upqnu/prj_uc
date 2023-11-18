package pp.rsmmm.domain.team.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pp.rsmmm.domain.team.dto.TeamCreateRequestDto;
import pp.rsmmm.domain.team.dto.TeamCreateResponseDto;
import pp.rsmmm.domain.team.service.TeamService;

@RequiredArgsConstructor
@RequestMapping("/api/teams")
@RestController
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/create")
    public ResponseEntity<TeamCreateResponseDto> createTeam(@RequestBody TeamCreateRequestDto teamCreateRequestDto) {
        TeamCreateResponseDto teamCreateResponseDto = teamService.createTeam(teamCreateRequestDto);
        return ResponseEntity.status(teamCreateResponseDto.getStatus()).body(teamCreateResponseDto);
    }
}
