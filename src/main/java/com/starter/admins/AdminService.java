package com.starter.admins;

import com.starter.admins.dto.requestDto.AdminCreateRequestDto;
import com.starter.admins.dto.responseDto.AdminResponseDto;
import com.starter.admins.entity.Admin;
import com.starter.admins.repository.AdminRepository;
import com.starter.common.config.PasswordEncoder;
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
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + email);
        }

        // 2-2. 역할 문자열을 Enum으로 변환 및 검증
        Admin.AdminRole role = Admin.AdminRole.valueOf(roleStr);

        // 3. 비밀번호 암호화
        String encryptionPassword = passwordEncoder.encode(password);

        // 4. 엔티티 준비
        Admin admin = new Admin(name, email, encryptionPassword,phone, role);

        // 5. 관리자 저장 & 반환
        Admin saveAdmin = adminRepository.save(admin);

        // 6. dto 생성 & 메세지 전달
        AdminResponseDto responseDto = new AdminResponseDto("관리자 신청이 완료되었습니다.");

        // 7. 반환
        return responseDto;


    }
}
