package com.lgcns.studify_be.service;

import com.lgcns.studify_be.common.NotFoundException;
import com.lgcns.studify_be.entity.Team;
import com.lgcns.studify_be.repository.TeamRepository;
// ✅ DTO들이 com.lgcns.studify_be.dto.team 아래에 있을 때의 import
import com.lgcns.studify_be.dto.team.TeamCreateRequest;
import com.lgcns.studify_be.dto.team.TeamUpdateRequest;
import com.lgcns.studify_be.dto.team.TeamResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok: final 필드 기반 생성자 자동 생성 (DI 용도)
public class TeamService {

    private final TeamRepository teamRepository; // JPA Repository 주입

    // ===== CREATE =====
    @Transactional // 쓰기 작업은 트랜잭션 필요
    public TeamResponse create(TeamCreateRequest req) {
        // 요청 DTO -> 엔티티 변환
        Team t = new Team();
        t.setName(req.name());
        t.setOwnerId(req.ownerId());
        t.setDescription(req.description());
        t.setMaxMembers(req.maxMembers());

        // 기본 상태값 (Enum 도입 전 임시)
        t.setVisibility("PUBLIC");
        t.setStatus("ACTIVE");

        // 저장 후 엔티티 -> 응답 DTO로 변환해서 반환
        return toResponse(teamRepository.save(t));
    }

    // ===== READ (Entity) =====
    @Transactional(readOnly = true) // 읽기 전용 힌트 -> 성능 최적화
    public Team findOne(Long id) {
        // 없으면 우리가 만든 NotFoundException 발생 -> GlobalExceptionHandler가 404로 변환
        return teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("team not found"));
    }

    // ===== READ (DTO - 단건) =====
    @Transactional(readOnly = true)
    public TeamResponse findOneResp(Long id) {
        return toResponse(findOne(id));
    }

    // ===== READ (DTO - 전체) =====
    @Transactional(readOnly = true)
    public List<TeamResponse> findAllResp() {
        // 전체 목록 조회 후 DTO로 맵핑
        return teamRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // ===== READ (DTO - 페이징) =====
    @Transactional(readOnly = true)
    public Page<TeamResponse> findAllPage(Pageable pageable) {
        // Page<Team> -> Page<TeamResponse>로 변환
        return teamRepository.findAll(pageable).map(this::toResponse);
    }

    // ===== UPDATE =====
    @Transactional
    public TeamResponse update(Long id, TeamUpdateRequest req) {
        // 대상 존재 확인 (없으면 404)
        Team t = findOne(id);

        // 변경할 값 반영 (Dirty Checking으로 flush)
        t.setName(req.name());
        t.setOwnerId(req.ownerId());
        t.setDescription(req.description());
        t.setMaxMembers(req.maxMembers());

        // save() 불필요 (영속 상태라 변경감지로 UPDATE 쿼리 나감)
        return toResponse(t);
    }

    // ===== DELETE =====
    @Transactional
    public void delete(Long id) {
        // 존재 확인 (없으면 404)
        Team t = findOne(id);
        // 실제 삭제
        teamRepository.delete(t);
    }

    // ===== Mapper: Entity -> DTO =====
    private TeamResponse toResponse(Team t) {
        return new TeamResponse(
                t.getId(),
                t.getName(),
                t.getOwnerId(),
                t.getDescription(),
                t.getVisibility(),
                t.getMaxMembers(),
                t.getStatus(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}
