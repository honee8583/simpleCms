package com.zerobase.cms.user.service.customer;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static com.zerobase.cms.user.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

    private final CustomerRepository customerRepository;

    /**
     * 회원가입
     */
    public Customer signUp(SignUpForm form) {
        return customerRepository.save(Customer.from(form));
    }

    public boolean isEmailExist(String email) {
        return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
                .isPresent();
    }

    /**
     * 이메일 인증 수행
     */
    @Transactional
    public void verifyEmail(String email, String code) {
        Customer customer = customerRepository.findByEmail(email)
                        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (customer.isVerify()) {
            throw new CustomException(ALREADY_VERIFIED);
        } else if (!customer.getVerificationCode().equals(code)) {
            throw new CustomException(WRONG_VERIFICATION);
        } else if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(EXPIRE_CODE);
        }

        customer.changeVerify();
    }

    /**
     * 이메일 인증요청정보 입력
     */
    @Transactional
    public LocalDateTime changeCustomerValidateEmail(Long customerId, String verificationCode) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        customer.emailVerification(verificationCode, LocalDateTime.now().plusDays(1));
        return customer.getVerifyExpiredAt();
    }
}
