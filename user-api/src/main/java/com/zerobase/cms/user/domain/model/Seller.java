package com.zerobase.cms.user.domain.model;

import com.zerobase.cms.user.domain.SignUpForm;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class Seller extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private String phone;
    private LocalDate birth;

    // 이메일 인증
    private LocalDateTime verifyExpiredAt;
    private String verificationCode;
    private boolean verify;

    public static Seller from(SignUpForm form) {
        return Seller.builder()
                .email(form.getEmail().toLowerCase(Locale.ROOT))
                .password(form.getPassword())
                .name(form.getName())
                .birth(form.getBirth())
                .phone(form.getPhone())
                .verify(false)
                .build();
    }

    public void emailVerification(String verificationCode, LocalDateTime now) {
        this.verificationCode = verificationCode;
        this.verifyExpiredAt = now;
    }

    public void changeVerify() {
        this.verify = true;
    }
}
