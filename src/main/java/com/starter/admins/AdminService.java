package com.starter.admins;

import com.starter.admins.dto.requestDto.AdminCreateRequestDto;
import com.starter.admins.dto.requestDto.AdminLoginRequestDto;
import com.starter.admins.dto.responseDto.AdminLoginResponseDto;
import com.starter.admins.dto.responseDto.AdminResponseDto;
import com.starter.admins.entity.Admin;
import com.starter.admins.repository.AdminRepository;
import com.starter.common.config.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new IllegalArgumentException("이미 존재하는 이메일입니다. ");
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
     *관리자 로그인 API
     */

    @Transactional(readOnly = true)
    public AdminLoginResponseDto loginAdmin(@Valid AdminLoginRequestDto requestDto, HttpServletRequest request) {
        // 1. 데이터 준비
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // 2. 이메일로 조회, 비밀번호 검증
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 틀렸습니다."));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        // 3. 계정 상태 검증 (활성 상태에만 통과)
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

        // 4. 세션 관리 (인증 성공 시 세션에 회원 정보 저장)
        HttpSession session = request.getSession(true); // 기존 세션이 없으면 새로 생성

        // 4-1. 세션에 관리자 정보 저장 (식별자 ID, 이메일, 역할)
        session.setAttribute("ADMIN_ID", admin.getAdminId());
        session.setAttribute("ADMIN_EMAIL", admin.getEmail());
        session.setAttribute("ADMIN_ROLE", admin.getRole());

        // 4-2. 세션 유효 시간 설정 (예: 24시간 = 24 * 60 * 60 초)
        session.setMaxInactiveInterval(24 * 60 * 60);

        // 5. 응답 DTO 생성
        AdminLoginResponseDto responseDto = new AdminLoginResponseDto("로그인에 성공하였습니다.");

        // 6. 응답 반환
        return responseDto;
    }

    /**
     *관리자 로그아웃 API
     */

    @Transactional
    public void logoutAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 서버 세션 무효화[cite: 3]
        }
    }
}


