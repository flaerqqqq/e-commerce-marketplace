package com.example.ecommercemarketplace.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user"})
@Entity
@Table(name = "login_data")
public class LoginData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "login_attempts")
    private int loginAttempts;

    @Column(name = "login_disabled")
    private boolean loginDisabled;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "last_login_attempt_time")
    private LocalDateTime lastLoginAttemptTime;

    @OneToOne(mappedBy = "loginData")
    private UserEntity user;

}
