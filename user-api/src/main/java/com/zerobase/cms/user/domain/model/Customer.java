package com.zerobase.cms.user.domain.model;

import com.zerobase.cms.user.domain.SignUpForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Customer extends BaseEntity{
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

    // 이런 static 메소드를 작성할경우 서비스단에서 코드를 보기 간결하다.
    public static Customer from(SignUpForm form) {
        return Customer.builder()
                .email(form.getEmail().toLowerCase(Locale.ROOT))
                .password(form.getPassword())
                .name(form.getName())
                .birth(form.getBirth())
                .phone(form.getPhone())
                .verify(false)
                .build();
    }


    // 인증

}
