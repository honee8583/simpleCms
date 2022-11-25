package com.zerobase.cms.user.service.seller;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.domain.repository.SellerRepository;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.zerobase.cms.user.exception.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    public Optional<Seller> findByIdAndEmail(Long id, String email) {
        return sellerRepository.findByIdAndEmail(id, email);
    }

    public Optional<Seller> findValidSeller(String email, String password) {
        return sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email, password);
    }

    /**
     * 회원가입
     */
    public Seller signUp(SignUpForm form) {
        return sellerRepository.save(Seller.from(form));
    }

    /**
     * 회원가입 확인
     */
    public boolean isEmailExist(String email) {
        return sellerRepository.findByEmail(email).isPresent();
    }

    /**
     * 이메일 인증 수행
     */
    @Transactional
    public void verifyEmail(String email, String code){
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (seller.isVerify()) {
            throw new CustomException(ErrorCode.ALREADY_VERIFIED);
        } else if (!seller.getVerificationCode().equals(code)) {
            throw new CustomException(ErrorCode.WRONG_VERIFICATION);
        } else if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRE_CODE);
        }

        seller.changeVerify();
    }

    /**
     * 이메일 인증요청정보 입력
     */
    @Transactional
    public LocalDateTime changeSellerValidateEmail(Long sellerId, String verificationCode) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        seller.emailVerification(verificationCode, LocalDateTime.now().plusDays(1));
        return seller.getVerifyExpiredAt();
    }
}
