package com.starter.admins.dto.service;

import com.starter.admins.dto.requestDto.AdminCreateRequestDto;
import com.starter.admins.dto.requestDto.AdminLoginRequestDto;
import com.starter.admins.dto.responseDto.AdminListResponseDto;
import com.starter.admins.dto.responseDto.AdminLoginResponseDto;
import com.starter.admins.dto.responseDto.AdminResponseDto;
import com.starter.admins.entity.Admin;
import com.starter.admins.repository.AdminRepository;
import com.starter.common.config.PasswordEncoder;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminService {
    //속성
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    //생성자

    //기능

    /**
     * 관리자 회원가입 API
     */

    @Transactional
    public AdminResponseDto createAdmin(AdminCreateRequestDto createRequestDto) {
        // 1. 데이터 준비
        String name = createRequestDto.getName();
        String email = createRequestDto.getEmail();
        String password = createRequestDto.getPassword();
        String phone = createRequestDto.getPhone();
        String roleStr = createRequestDto.getRole();

        // 2. 데이터 검증
        // 2-1. 중복된 이메일 확인 (가입된 이메일이 존재하면 예외처리)
        if (adminRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 2-2. 역할 문자열을 Enum으로 변환 및 검증
        Admin.AdminRole role = Admin.AdminRole.valueOf(roleStr);

        // 3. 비밀번호 암호화
        String encryptionPassword = passwordEncoder.encode(password);

        // 4. 엔티티 준비
        Admin admin = new Admin(name, email, encryptionPassword, phone, role);

        // 5. 관리자 저장 & 반환
        Admin saveAdmin = adminRepository.save(admin);

        // 6. dto 생성 & 메세지 전달
        AdminResponseDto responseDto = new AdminResponseDto("관리자 신청이 완료되었습니다.");

        // 7. 반환
        return responseDto;
    }

    /**
     * 관리자 로그인 API
     */


    @Transactional(readOnly = true)
    public AdminLoginResponseDto loginAdmin(AdminLoginRequestDto requestDto, HttpSession session) {
        // 1. 데이터 준비
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // 2. DB 유저 이메일로 조회 -> Admin 엔티티
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다."));

        // 3. 비밀번호 비교 검증
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다.");
        }

        // 4. 계정 상태 검증 (활성 상태에만 통과)
        switch (admin.getStatus()) {
            case PENDING:
                throw new IllegalArgumentException("계정 승인 대기 중 입니다.");
            case REJECTED:
                throw new IllegalArgumentException("계정 신청이 거부 됐습니다.");
            case SUSPENDED:
                throw new IllegalArgumentException("계정이 정지 됐습니다.");
            case DEACTIVATE:
                throw new IllegalArgumentException("계정 비활성화됐습니다.");
            case ACTIVATE:
                // 활성 상태일 경우 정상 통과
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 계정 상태입니다.");
        }

        // 5. 세션에 관리자 정보 데이터 생성(식별자 ID, 이메일, 역할)
        session.setAttribute("ADMIN_ID", admin.getAdminId());
        session.setAttribute("ADMIN_EMAIL", admin.getEmail());
        session.setAttribute("ADMIN_ROLE", admin.getRole());

        // 6. 응답 생성
        AdminLoginResponseDto response = new AdminLoginResponseDto("로그인에 성공하였습니다.");

        // 7. 응답 반환
        return response;
    }


    /**
     * 관리자 로그아웃 API
     */

    public void logoutAdmin(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * 관리자 다건 조회 API
     */

    @Transactional
    public AdminListResponseDto getAdminList(
            String keyword,
            int page,
            int limit,
            String sortBy,
            String sortOrder,
            Admin.AdminRole role,
            Admin.AdminStatus status
    ) {
       //1. 정렬 방향 변환("asc"/"desc" 문자열을 변환)
        Sort.Direction sortDirection = Sort.Direction.fromString(sortOrder);

        // 2. 정렬 조건 생성
        Sort sortConditions = Sort.by(sortDirection,sortBy);

        // 3. 페이징 조건 생성
        Pageable pageableConditions = PageRequest.of(page -1, limit, sortConditions);

        //4. 검색어 조건 생성 &
        String searchKeyword = (keyword == null || keyword.isBlank()) ? null : keyword;

        // 5. 페이징이 적용된 관리자 목록  조회
        Page<Admin> adminPage = adminRepository.findByConditions(searchKeyword, role, status, pageableConditions);

        // 6. 내부 Dto 생성
        List<AdminListResponseDto.AdminDto> adminDtoList = adminPage.getContent().stream()
                .map((admin) -> new AdminListResponseDto.AdminDto(
                        admin.getAdminId(),
                        admin.getName(),
                        admin.getEmail(),
                        admin.getPhone(),
                        admin.getRole(),
                        admin.getStatus(),
                        admin.getCreatedAt(),
                        admin.getApprovedAt()
                ))
                .collect(Collectors.toList());

        // 7. 외부 Dto 생성
        AdminListResponseDto response = new AdminListResponseDto(
                adminDtoList,
                adminPage.getNumber() + 1,
                adminPage.getSize(),
                adminPage.getTotalElements(),
                adminPage.getTotalPages()
        );

        // 8. 반환
        return response;
    }
}






