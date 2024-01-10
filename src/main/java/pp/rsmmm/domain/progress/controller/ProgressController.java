package pp.rsmmm.domain.progress.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pp.rsmmm.domain.progress.dto.ProgressCreateRequestDto;
import pp.rsmmm.domain.progress.dto.ProgressCreateResponseDto;
import pp.rsmmm.domain.progress.dto.ProgressNameModifyDto;
import pp.rsmmm.domain.progress.dto.ProgressOrderModifyDto;
import pp.rsmmm.domain.progress.entity.Progress;
import pp.rsmmm.domain.progress.service.ProgressService;

@RequiredArgsConstructor
@RequestMapping("/api/teams/{teamId}/progresses")
@RestController
@Tag(name = "Progress", description = "Progress(진행상황 ; 칸반보드에서 column 역할) API")
public class ProgressController {

    private final ProgressService progressService;

    /**
     * 진행 상황(Progress - 칸반보드 내 column) 생성
     * @param teamId
     * @param progressCreateRequestDto
     * @return
     */
    @Operation(summary = "진행상황 생성", description = "특정 팀에서 진행상황을 생성합니다")
    @Parameter(name = "teamId / progressCreateRequestDto", description = "진행상황의 이름 필수 입력(다른 입력사항 없음)")
    @PostMapping("/create")
    public ResponseEntity<ProgressCreateResponseDto> createProgress(
            @PathVariable Long teamId, @RequestBody ProgressCreateRequestDto progressCreateRequestDto
    ) {
        ProgressCreateResponseDto progressCreateResponseDto = progressService.createProgress(teamId, progressCreateRequestDto);
        return ResponseEntity.status(progressCreateResponseDto.getStatus()).body(progressCreateResponseDto);
    }

    /**
     * 진행상황(Progress) 조회
     * @param progressId
     * @return
     */
    @Operation(summary = "진행상황 조회", description = "특정 팀에 포함된 진행상황을 조회합니다")
    @Parameter(name = "progressId")
    @GetMapping("/{progressId}")
    public ResponseEntity<Progress> getProgress(@PathVariable Long teamId, @PathVariable Long progressId) {
        Progress progress = progressService.getProgress(teamId, progressId);
        return ResponseEntity.ok(progress);
    }

    /**
     * 진행상황(Progress) 삭제
     * @param teamId
     * @param progressId
     * @return
     */
    @Operation(summary = "진행상황 삭제", description = "특정 팀에 포함된 진행상황을 삭제합니다")
    @Parameter(name = "progressId")
    @DeleteMapping("/{progressId}")
    public ResponseEntity<String> deleteProgress(@PathVariable Long teamId, @PathVariable Long progressId) {
        progressService.deleteProgress(teamId, progressId);
        return ResponseEntity.ok("진행상황 삭제가 완료되었습니다.");
    }

    /**
     * 진행상황(Progress) 이름 변경
     * @param progressNameModifyDto
     * @param teamId
     * @param progressId
     * @return
     */
    @Operation(summary = "진행상황 이름 변경", description = "특정 팀에 포함된 진행상황의 이름을 변경합니다")
    @Parameter(name = "progressNameModifyDto / teamId / progressId", description = "새로운 진행상황 이름 필수 입력")
    @PutMapping("/{progressId}")
    public ResponseEntity<Progress> modifyProgressName(
            @RequestBody ProgressNameModifyDto progressNameModifyDto,
            @PathVariable Long teamId, @PathVariable Long progressId
    ) {
        Progress progress = progressService.modifyProgressName(progressNameModifyDto, teamId, progressId);
        return ResponseEntity.ok(progress);
    }

    /**
     * 진행상황(Progress) 순서 변경
     * @param progressNameModifyDto
     * @param teamId
     * @param progressId
     * @return
     */
    @Operation(summary = "팀 내 진행상황 순서를 변경", description = "특정 팀에 포함된 진행상황의 순서를 변경합니다")
    @Parameter(name = "ProgressOrderModifyDto / teamId / progressId", description = "진행상황의 새로운 순서 필수 입력")
    @PatchMapping("/{progressId}")
    public ResponseEntity<Progress> modifyProgressOrder(
            @RequestBody ProgressOrderModifyDto progressNameModifyDto,
            @PathVariable Long teamId, @PathVariable Long progressId
    ) {
        Progress progress = progressService.modifyProgressOrder(progressNameModifyDto, teamId, progressId);
        return ResponseEntity.ok(progress);
    }

}
