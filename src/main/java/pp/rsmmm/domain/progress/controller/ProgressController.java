package pp.rsmmm.domain.progress.controller;

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
public class ProgressController {

    private final ProgressService progressService;

    /**
     * 진행 상황(Progress - 칸반보드 내 column) 생성
     * @param teamId
     * @param progressCreateRequestDto
     * @return
     */
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
    @PatchMapping("/{progressId}")
    public ResponseEntity<Progress> modifyProgressOrder(
            @RequestBody ProgressOrderModifyDto progressNameModifyDto,
            @PathVariable Long teamId, @PathVariable Long progressId
    ) {
        Progress progress = progressService.modifyProgressOrder(progressNameModifyDto, teamId, progressId);
        return ResponseEntity.ok(progress);
    }

}
