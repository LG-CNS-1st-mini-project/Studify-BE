package com.lgcns.studify_be.controller;

import com.lgcns.studify_be.dto.team.TeamCreateRequest;
import com.lgcns.studify_be.dto.team.TeamResponse;
import com.lgcns.studify_be.dto.team.TeamUpdateRequest;
import com.lgcns.studify_be.service.TeamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid; // ✅ Bean Validation 활성화

import java.net.URI;

@RestController
@RequestMapping("/studify/api/v1/teams")        // 팀 관련 REST 엔드포인트 루트
@RequiredArgsConstructor             // final 필드 기반 생성자 자동 생성
public class TeamController {

    private final TeamService teamService;

    // ===== CREATE =====
    @Operation(summary = "팀 생성", description = "새로운 팀을 생성합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "생성됨",
            content = @Content(schema = @Schema(implementation = TeamResponse.class))),
        @ApiResponse(responseCode = "400", description = "요청 검증 실패")
    })
    @PostMapping
    public ResponseEntity<TeamResponse> create(
            // ✅ @Valid: DTO에 선언된 유효성 검사(@NotBlank 등)를 실행
            @RequestBody @Valid TeamCreateRequest req
    ) {
        TeamResponse res = teamService.create(req);
        URI location = URI.create("/api/teams/" + res.id());
        return ResponseEntity.created(location).body(res);
    }

    // ===== READ (단건) =====
    @Operation(summary = "팀 단건 조회", description = "ID로 특정 팀을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = TeamResponse.class))),
        @ApiResponse(responseCode = "404", description = "대상 없음")
    })
    @GetMapping("/{id}")
    public TeamResponse get(@PathVariable Long id) {
        return teamService.findOneResp(id);
    }

    // ===== READ (목록 - 페이징) =====
    @Operation(summary = "팀 목록 조회(페이징)", description = "페이지/사이즈/정렬을 적용하여 팀 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping
    public Page<TeamResponse> list(
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return teamService.findAllPage(pageable);
    }

    // ===== UPDATE =====
    @Operation(summary = "팀 수정", description = "팀 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정됨",
            content = @Content(schema = @Schema(implementation = TeamResponse.class))),
        @ApiResponse(responseCode = "400", description = "요청 검증 실패"),
        @ApiResponse(responseCode = "404", description = "대상 없음")
    })
    @PutMapping("/{id}")
    public TeamResponse update(
            @PathVariable Long id,
            // ✅ 수정 시에도 @Valid 추가
            @RequestBody @Valid TeamUpdateRequest req
    ) {
        return teamService.update(id, req);
    }

    // ===== DELETE =====
    @Operation(summary = "팀 삭제", description = "팀을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "삭제됨"),
        @ApiResponse(responseCode = "404", description = "대상 없음")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        teamService.delete(id);
    }
}
