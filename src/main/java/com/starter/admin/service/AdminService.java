package com.starter.admin.service;

import com.starter.admin.exception.AdminException;
import com.starter.admin.dto.request.*;
import com.starter.admin.dto.response.*;
import com.starter.admin.entity.Admin;
import com.starter.admin.enums.AdminErrorCode;
import com.starter.admin.enums.AdminRole;
import com.starter.admin.enums.AdminStatus;
import com.starter.admin.repository.AdminRepository;
import com.starter.common.config.PasswordEncoder;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 관리자 서비스 비즈니스 로직 처리 클래스
 */
@Service
public class AdminService {
    //속성
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    //생성자

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //기능

    /**
     * 관리자 회원가입 처리
     *
     * @param createRequest 관리자 회원가입 요청 DTO
     * @return 가입 완료 메시지 응답 DTO
     */
    @Transactional
    public AdminMessageResponse createAdmin(AdminCreateRequest createRequest) {
        // 1. 데이터 준비
        String adminName = createRequest.getAdminName();
        String adminEmail = createRequest.getAdminEmail();
        String password = createRequest.getPassword();
        String adminPhone = createRequest.getAdminPhone();
        AdminRole role = createRequest.getRole();

        // 2. 데이터 검증 (중복 이메일 확인)
        if (adminRepository.existsByAdminEmail(adminEmail)) {
            throw new AdminException(AdminErrorCode.ALREADY_EXISTS);
        }

        // 3. 비밀번호 암호화
        String encryptionPassword = passwordEncoder.encode(password);

        // 4. 엔티티 준비
        Admin admin = new Admin(adminName, adminEmail, encryptionPassword, adminPhone, role);

        // 5. 관리자 저장
        adminRepository.save(admin);

        // 6. dto 생성 & 메세지 전달
        AdminMessageResponse response = new AdminMessageResponse("관리자 신청이 완료되었습니다.");

        // 7. 반환
        return response;
    }

    /**
     * 관리자 로그인 처리
     *
     * @param loginRequest 로그인 요청 DTO
     * @param session      HTTP 세션
     * @return 로그인 성공 응답 DTO
     */
    @Transactional(readOnly = true)
    public AdminMessageResponse loginAdmin(AdminLoginRequest loginRequest, HttpSession session) {
        // 1. 데이터 준비
        String adminEmail = loginRequest.getAdminEmail();
        String password = loginRequest.getPassword();

        // 2. DB 유저 이메일로 조회 -> Admin 엔티티
        Admin admin = adminRepository.findByAdminEmail(adminEmail)
                .orElseThrow(() -> new AdminException(AdminErrorCode.UNAUTHORIZED_USER));

        // 3. 비밀번호 비교 검증
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new AdminException(AdminErrorCode.UNAUTHORIZED_USER);
        }

        // 4. 계정 상태 검증 (활성 상태에만 통과)
        switch (admin.getAdminStatus()) {
            case PENDING:
                throw new AdminException(AdminErrorCode.UNAUTHORIZED_USER);
            case REJECTED:
                throw new AdminException(AdminErrorCode.UNAUTHORIZED_USER, admin.getRejectReason());
            case SUSPENDED:
                throw new AdminException(AdminErrorCode.UNAUTHORIZED_USER);
            case DEACTIVATE:
                throw new AdminException(AdminErrorCode.UNAUTHORIZED_USER);
            case ACTIVATE:
                // 활성 상태일 경우 정상 통과
                break;
            default:
                throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 5. 세션에 관리자 정보 데이터 생성(식별자 ID, 이메일, 역할)
        session.setAttribute("ADMIN_ID", admin.getAdminId());
        session.setAttribute("ADMIN_EMAIL", admin.getAdminEmail());
        session.setAttribute("ADMIN_ROLE", admin.getRole());

        // 6. 응답 생성
        AdminMessageResponse loginResponse = new AdminMessageResponse("로그인에 성공하였습니다.");

        // 7. 응답 반환
        return loginResponse;
    }


    /**
     * 관리자 로그아웃 처리
     *
     * @param session HTTP 세션
     */
    @Transactional
    public AdminMessageResponse logoutAdmin(HttpSession session) {
        // 1. 로그아웃 처리
        if (session != null) {
            session.invalidate();
        }

        // 2. 응답 생성
        AdminMessageResponse response = new AdminMessageResponse("로그아웃 되었습니다.");

        // 3. 응답 반환
        return response;
    }

    /**
     * 관리자 다건 조회 처리
     *
     * @param keyword      검색 키워드
     * @param page         페이지 번호
     * @param limit        페이지 당 항목 수
     * @param sortBy       정렬 기준 필드
     * @param sortOrder    정렬 방향 (asc/desc)
     * @param role         관리자 역할 필터
     * @param adminStatus       관리자 상태 필터
     * @param loginAdminId 로그인한 관리자 ID
     * @return 관리자 목록 응답 DTO
     */
    @Transactional(readOnly = true)
    public AdminListResponse getAdminList(
            String keyword,
            int page,
            int limit,
            String sortBy,
            String sortOrder,
            AdminRole role,
            AdminStatus adminStatus,
            Long loginAdminId
    ) {
        // 1. 로그인한 관리자 조회
        Admin foundLoginAdmin = adminRepository.findById(loginAdminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 2. 권한 검증(슈퍼 관리자만 조회 가능)
        if (foundLoginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new AdminException(AdminErrorCode.PERMISSION_DENIED);
        }

        // 3. 정렬 방향 변환("asc"/"desc" 문자열을 변환)
        Sort.Direction sortDirection = Sort.Direction.fromString(sortOrder);

        // 4. 정렬 조건 생성
        Sort sortConditions = Sort.by(sortDirection, sortBy);

        // 5. 페이징 조건 생성
        Pageable pageableConditions = PageRequest.of(page - 1, limit, sortConditions);

        // 6. 검색어 조건 생성 &
        String searchKeyword = (keyword == null || keyword.isBlank()) ? null : keyword;

        // 7. 페이징이 적용된 관리자 목록  조회
        Page<Admin> adminPage = adminRepository.findByConditions(searchKeyword, role, adminStatus, pageableConditions);

        // 8. 내부 Dto 생성
        List<AdminListResponse.AdminResponse> adminResponseList = adminPage.getContent().stream()
                .map((admin) -> new AdminListResponse.AdminResponse(
                        admin.getAdminId(),
                        admin.getAdminName(),
                        admin.getAdminEmail(),
                        admin.getAdminPhone(),
                        admin.getRole(),
                        admin.getAdminStatus(),
                        admin.getCreatedAt(),
                        admin.getActivatedAt()
                ))
                .collect(Collectors.toList());

        // 9. 응답(외부 dto) 생성
        AdminListResponse response = new AdminListResponse(
                adminResponseList,
                adminPage.getNumber() + 1,
                adminPage.getSize(),
                adminPage.getTotalElements(),
                adminPage.getTotalPages()
        );

        // 10. 반환
        return response;
    }

    /**
     * 관리자 상세 조회 처리
     *
     * @param loginAdminId 로그인한 관리자 ID
     * @param adminId      조회할 관리자 ID
     * @return 관리자 상세 정보 응답 DTO
     */
    @Transactional(readOnly = true)
    public AdminGetInfoResponse getAdminInfo(Long loginAdminId, Long adminId) {
        // 1. 로그인한 관리자 조회
        Admin foundLoginAdmin = adminRepository.findById(loginAdminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 2. 권한 검증(슈퍼 관리자만 역할 변경 가능)
        if (foundLoginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new AdminException(AdminErrorCode.PERMISSION_DENIED);
        }

        // 3. 관리자 조회
        Admin foundAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 4. 조회된 관리자 데이터 준비
        String adminName = foundAdmin.getAdminName();
        String adminEmail = foundAdmin.getAdminEmail();
        String adminPhone = foundAdmin.getAdminPhone();
        AdminRole role = foundAdmin.getRole();
        AdminStatus adminStatus = foundAdmin.getAdminStatus();
        LocalDateTime createdAt = foundAdmin.getCreatedAt();
        LocalDateTime activatedAt = foundAdmin.getActivatedAt();

        // 5. 응답(dto) 생성
        AdminGetInfoResponse response = new AdminGetInfoResponse(
                adminName,
                adminEmail,
                adminPhone,
                role,
                adminStatus,
                createdAt,
                activatedAt
        );

        // 6. 반환
        return response;

    }

    /**
     * 관리자 정보 수정 처리
     *
     * @param loginAdminId           로그인한 관리자 ID
     * @param adminId                수정할 관리자 ID
     * @param adminUpdateInfoRequest 수정할 정보 DTO
     * @return 수정된 관리자 정보 응답 DTO
     */
    @Transactional
    public AdminUpdateInfoResponse updateAdminInfo(
            Long loginAdminId,
            Long adminId,
            AdminUpdateInfoRequest adminUpdateInfoRequest
    ) {
        // 1. 로그인한 관리자 조회
        Admin foundLoginAdmin = adminRepository.findById(loginAdminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 2. 권한 검증(슈퍼 관리자만 역할 변경 가능)
        if (foundLoginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new AdminException(AdminErrorCode.PERMISSION_DENIED);
        }

        // 3. 관리자 조회
        Admin foundAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 4. 데이터 준비
        String newAdminName = adminUpdateInfoRequest.getAdminName();
        String newAdminEmail = adminUpdateInfoRequest.getAdminEmail();
        String newAdminPhone = adminUpdateInfoRequest.getAdminPhone();

        // 5. 수정하기
        Admin updateAdmin = foundAdmin.changeInfo(newAdminName, newAdminEmail, newAdminPhone);

        // 6. 수정 데이터 준비
        String updateAdminName = updateAdmin.getAdminName();
        String updateAdminEmail = updateAdmin.getAdminEmail();
        String updateAdminPhone = updateAdmin.getAdminPhone();
        LocalDateTime updateAdminCreatedAt = updateAdmin.getCreatedAt();
        LocalDateTime updateAdminUpdateAT = updateAdmin.getUpdatedAt();

        // 7. 응답(dto) 생성
        AdminUpdateInfoResponse response = new AdminUpdateInfoResponse(
                updateAdminName,
                updateAdminEmail,
                updateAdminPhone,
                updateAdminCreatedAt,
                updateAdminUpdateAT
        );

        // 8. 반환
        return response;
    }

    /**
     * 관리자 역할 변경 처리
     *
     * @param loginAdminId           로그인한 관리자 ID
     * @param adminId                대상 관리자 ID
     * @param adminUpdateRoleRequest 역할 변경 요청 DTO
     * @return 역할 변경 결과 응답 DTO
     */
    @Transactional
    public AdminUpdateRoleResponse updateAdminRole(
            Long loginAdminId,
            Long adminId,
            AdminUpdateRoleRequest adminUpdateRoleRequest
    ) {
        // 1. 로그인한 관리자 조회
        Admin foundLoginAdmin = adminRepository.findById(loginAdminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.PERMISSION_DENIED));

        // 2. 권한 검증(슈퍼 관리자만 역할 변경 가능)
        if (foundLoginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new AdminException(AdminErrorCode.PERMISSION_DENIED);
        }

        // 3. 관리자 조회
        Admin foundAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 4. 데이터 준비
        AdminRole newRole = adminUpdateRoleRequest.getRole();

        // 5. 수정하기
        Admin updateAdminRole = foundAdmin.changeRole(newRole);

        // 6. 수정된 데이터 준비
        Long updateAdminId = updateAdminRole.getAdminId();
        String adminName = updateAdminRole.getAdminName();
        AdminRole updateAdminNewRole = updateAdminRole.getRole();
        LocalDateTime updateAt = updateAdminRole.getUpdatedAt();

        // 7. 응답(dto) 생성
        AdminUpdateRoleResponse response = new AdminUpdateRoleResponse(
                updateAdminId,
                adminName,
                updateAdminNewRole,
                updateAt
        );

        // 8. 반환
        return response;
    }


    /**
     * 관리자 상태 변경 처리
     *
     * @param loginAdminId             로그인한 관리자 ID
     * @param adminId                  대상 관리자 ID
     * @param adminUpdatestatusRequest 상태 변경 요청 DTO
     * @return 상태 변경 결과 응답 DTO
     */
    @Transactional
    public AdminUpdateStatusResponse updateAdminStatus(
            Long loginAdminId,
            Long adminId,
            AdminUpdateStatusRequest adminUpdatestatusRequest
    ) {
        // 1. 로그인한 관리자 조회
        Admin foundLoginAdmin = adminRepository.findById(loginAdminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 2. 권한 검증(슈퍼 관리자만 상태 변경 가능)
        if (foundLoginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new AdminException(AdminErrorCode.PERMISSION_DENIED);
        }

        // 3. 관리자 조회
        Admin foundAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.PERMISSION_DENIED));

        // 4. 바꿀 상태와 거부 사유 준비
        AdminStatus newStatus = adminUpdatestatusRequest.getAdminStatus();
        String rejectReason = adminUpdatestatusRequest.getRejectReason();

        // 5. 변경 상태가 거부일 때
        if (newStatus == AdminStatus.REJECTED) {
            // 5-1. 거부 사유 필수 검증
            if (rejectReason == null || rejectReason.isBlank()) {
                throw new AdminException(AdminErrorCode.INVALID_REQUEST);
            }
        }

        // 6. 수정 사항 저장하기
        Admin updateAdminStatus = foundAdmin.changeStatus(newStatus, rejectReason);

        // 7. 수정 데이터 준비
        Long updateAdminId = updateAdminStatus.getAdminId();
        String adminName = updateAdminStatus.getAdminName();
        AdminStatus updateAdminNewStatus = updateAdminStatus.getAdminStatus();
        LocalDateTime updateAt = updateAdminStatus.getUpdatedAt();

        // 8. 응답(dto) 생성
        AdminUpdateStatusResponse response = new AdminUpdateStatusResponse(
                updateAdminId,
                adminName,
                updateAdminNewStatus,
                updateAt
        );

        // 9. 반환
        return response;
    }

    /**
     * 관리자 삭제 처리
     *
     * @param loginAdminId 로그인한 관리자 ID
     * @param adminId      삭제할 관리자 ID
     * @return 삭제 완료 메시지 응답 DTO
     */
    @Transactional
    public AdminMessageResponse deleteAdmin(Long loginAdminId, Long adminId) {
        // 1. 로그인한 관리자 조회
        Admin foundLoginAdmin = adminRepository.findById(loginAdminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 2. 권한 검증(슈퍼 관리자만 상태 변경 가능)
        if (foundLoginAdmin.getRole() != AdminRole.SUPER_ADMIN) {
            throw new AdminException(AdminErrorCode.PERMISSION_DENIED);
        }

        // 3. 관리자 조회
        Admin foundAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 4. 삭제 처리(공용 엔티티 사용)
        foundAdmin.softDelete();

        // 5. 응답(dto) 생성
        AdminMessageResponse response = new AdminMessageResponse("삭제에 성공하였습니다.");

        // 6. 응답 반환
        return response;
    }

    /**
     * 내 정보 조회 처리
     *
     * @param loginAdminId 로그인한 관리자 ID
     * @return 내 정보 응답 DTO
     */
    @Transactional(readOnly = true)
    public AdminGetMyInfoResponse getAdminProfile(Long loginAdminId) {
        // 1. 로그인한 관리자 조회
        Admin foundLoginAdmin = adminRepository.findById(loginAdminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 2. 데이터 준비
        String adminName = foundLoginAdmin.getAdminName();
        String adminEmail = foundLoginAdmin.getAdminEmail();
        String adminPhone = foundLoginAdmin.getAdminPhone();
        LocalDateTime createdAt = foundLoginAdmin.getCreatedAt();
        LocalDateTime activatedAt = foundLoginAdmin.getActivatedAt();

        // 3. 응답 (dto) 생성
        AdminGetMyInfoResponse response = new AdminGetMyInfoResponse(
                adminName,
                adminEmail,
                adminPhone,
                createdAt,
                activatedAt
        );

        // 4. 반환
        return response;

    }

    /**
     * 내 정보 수정 처리
     *
     * @param loginAdminId           로그인한 관리자 ID
     * @param adminUpdateInfoRequest 수정할 정보 DTO
     * @return 수정된 내 정보 응답 DTO
     */
    @Transactional
    public AdminUpdateInfoResponse updateAdminProfileInfo(
            Long loginAdminId,
            AdminUpdateInfoRequest adminUpdateInfoRequest
    ) {
        // 1. 로그인한 관리자 조회
        Admin foundLoginAdmin = adminRepository.findById(loginAdminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 2. 데이터 준비
        String newAdminName = adminUpdateInfoRequest.getAdminName();
        String newAdminEmail = adminUpdateInfoRequest.getAdminEmail();
        String newAdminPhone = adminUpdateInfoRequest.getAdminPhone();

        // 3. 수정하기
        Admin updateAdminInfo = foundLoginAdmin.changeInfo(newAdminName, newAdminEmail, newAdminPhone);

        // 4. 수정 데이터 준비
        String updateAdminName = updateAdminInfo.getAdminName();
        String updateAdminEmail = updateAdminInfo.getAdminEmail();
        String updateAdminPhone = updateAdminInfo.getAdminPhone();
        LocalDateTime updateAdminCreatedAt = updateAdminInfo.getCreatedAt();
        LocalDateTime updateAdminUpdateAT = updateAdminInfo.getUpdatedAt();

        // 5. 응답(dto) 생성
        AdminUpdateInfoResponse response = new AdminUpdateInfoResponse(
                updateAdminName,
                updateAdminEmail,
                updateAdminPhone,
                updateAdminCreatedAt,
                updateAdminUpdateAT
        );

        // 6. 반환
        return response;
    }

    /**
     * 내 비밀번호 수정 처리
     *
     * @param loginAdminId          로그인한 관리자 ID
     * @param updatePasswordRequest 비밀번호 변경 요청 DTO
     * @return 변경 완료 메시지 응답 DTO
     */
    @Transactional
    public AdminMessageResponse updatePassword(Long loginAdminId, AdminUpdatePasswordRequest updatePasswordRequest) {
        // 1. 비밀번호 데이터 준비
        String password = updatePasswordRequest.getOldPassword();

        // 2. 로그인한 관리자 조회
        Admin foundLoginAdmin = adminRepository.findById(loginAdminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.RESOURCE_NOT_FOUND));

        // 3. 현재 비밀번호 검증
        if (!passwordEncoder.matches(password, foundLoginAdmin.getPassword())) {
            throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 4. 입력한 새 비밀번호 입력 확인
        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getNewPasswordConfirm())) {
            throw new AdminException(AdminErrorCode.INVALID_REQUEST);
        }

        // 5. 새 비밀번호 데이터 준비
        String encodedNewPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());

        // 6. 수정된 새 비밀번호로 저장
        foundLoginAdmin.changePassword(encodedNewPassword);

        // 7. 응답(dto) 생성
        AdminMessageResponse response = new AdminMessageResponse("비밀번호 변경에 성공하였습니다.");

        // 8. 반환
        return response;
    }

}








