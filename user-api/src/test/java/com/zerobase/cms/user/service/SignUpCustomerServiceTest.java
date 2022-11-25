package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.customer.SignUpCustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Transactional
@ExtendWith(MockitoExtension.class)
class SignUpCustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    SignUpCustomerService signUpCustomerService;

    private SignUpForm createSignUpForm() {
        return SignUpForm.builder()
                .email("test@test.com")
                .name("test")
                .password("test")
                .phone("010-0000-0000")
                .build();
    }

    private Customer createCustomer() {
        return Customer.builder()
                .id(2L)
                .email("test@test.com")
                .name("test")
                .password("test")
                .phone("010-0000-0000")
                .verify(true)
                .build();
    }

    private Customer createVerifiedCustomer() {
        return Customer.builder()
                .id(2L)
                .email("test@test.com")
                .name("test")
                .password("test")
                .phone("010-0000-0000")
                .verify(false)
                .verificationCode("verificationCode")
                .verifyExpiredAt(LocalDateTime.now().plusDays(1))
                .build();
    }

    @Test
    void testSignUp() {
        // given
        SignUpForm form = createSignUpForm();
        Customer customer = createCustomer();

        given(customerRepository.save(any()))
                .willReturn(customer);

        // when
        Customer savedCustomer = signUpCustomerService.signUp(form);

        // then
        assertEquals(savedCustomer.getId(), 2L);
        assertEquals(savedCustomer.getEmail(), "test@test.com");
        assertEquals(savedCustomer.getName(), "test");
        assertEquals(savedCustomer.getPassword(), "test");
        assertEquals(savedCustomer.getPhone(), "010-0000-0000");
    }

    @Test
    void testIsEmailExist() {
        // given
        Customer customer = createCustomer();
        String email = customer.getEmail();

        given(customerRepository.findByEmail(anyString()))
                .willReturn(Optional.of(customer));

        // when
        boolean result = signUpCustomerService.isEmailExist(email);

        // then
        assertTrue(result);

    }

    @Test
    void testVerifyEmail() {
        // given
        Customer customer = createVerifiedCustomer();

        given(customerRepository.findByEmail(anyString()))
                .willReturn(Optional.of(customer));

        // when
        signUpCustomerService.verifyEmail(customer.getEmail(), customer.getVerificationCode());

        // then
        assertTrue(customer.isVerify());
    }

    @Test
    @DisplayName("Verify Email CustomException -> ALREADY_VERIFIED")
    void testVerifyEmailAlreadyVerifiedException() {
        // given
        Customer customer = createVerifiedCustomer();
        customer.changeVerify();

        given(customerRepository.findByEmail(anyString()))
                .willReturn(Optional.of(customer));

        // when
        CustomException customException =
                assertThrows(CustomException.class,
                        () -> signUpCustomerService
                                .verifyEmail(customer.getEmail(), customer.getVerificationCode()));

        // then
        assertEquals(ErrorCode.ALREADY_VERIFIED, customException.getErrorCode());
    }

    @Test
    @DisplayName("Verify Email CustomException -> WRONG_VERIFICATION")
    void testVerifyEmailWrongVerificationException() {
        // given
        Customer customer = createVerifiedCustomer();

        given(customerRepository.findByEmail(anyString()))
                .willReturn(Optional.of(customer));

        // when
        CustomException customException =
                assertThrows(CustomException.class,
                        () -> signUpCustomerService
                                .verifyEmail(customer.getEmail(), "wrongCode"));

        // then
        assertEquals(ErrorCode.WRONG_VERIFICATION, customException.getErrorCode());
    }

    @Test
    @DisplayName("Verify Email CustomException -> EXPIRE_CODE")
    void testVerifyEmailExpireCode() {
        // given
        Customer customer = Customer.builder()
                            .email("test@test.com")
                            .verifyExpiredAt(LocalDateTime.now().minusDays(2))
                            .verificationCode("verificationCode")
                            .build();

        given(customerRepository.findByEmail(anyString()))
                .willReturn(Optional.of(customer));

        // when
        CustomException customException =
                assertThrows(CustomException.class,
                        () -> signUpCustomerService
                                .verifyEmail(customer.getEmail(), customer.getVerificationCode()));

        // then
        assertEquals(ErrorCode.EXPIRE_CODE, customException.getErrorCode());
    }

    @Test
    void testChangeCustomerValidateEmail() {
        // given
        Customer customer = Customer.builder()
                                    .id(2L)
                                    .email("test@test.com")
                                    .verify(false)
                                    .build();

        given(customerRepository.findById(anyLong()))
                .willReturn(Optional.of(customer));

        // when
        LocalDateTime expiredAtTime =
                signUpCustomerService.changeCustomerValidateEmail(2L, "changeCode");

        // then
        assertEquals(customer.getVerificationCode(), "changeCode");
    }

}