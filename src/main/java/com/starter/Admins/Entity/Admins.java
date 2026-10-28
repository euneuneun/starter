package com.starter.Admins.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "admins")
public class Admins {

    //속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 30)
    private String role;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = true, length = 200)
    private String rejectReason;

    @Column(nullable = true)
    private LocalDateTime approvedAt;

    @Column(nullable = false)
    private LocalDateTime rejectedAt;

    //생성자
    public Admins(
            Long id,
            String name,
            String email,
            String password,
            String phone,
            String role,
            String status,
            String rejectReason,
            LocalDateTime approvedAt,
            LocalDateTime rejectedAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.rejectReason = rejectReason;
        this.approvedAt = approvedAt;
        this.rejectedAt = rejectedAt;
    }











}
