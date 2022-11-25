package com.zerobase.cms.user.service.customer;

import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@Transactional
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer createCustomer() {
        Customer customer = Customer.builder()
                .id(2L)
                .email("test@test.com")
                .name("test")
                .phone("010-0000-0000")
                .birth(LocalDate.now())
                .password("test")
                .build();

        return customer;
    }

    @Test
    void testFindByIdAndEmail() {
        // given
        Customer customer = createCustomer();

        given(customerRepository.findByIdAndEmail(anyLong(), anyString()))
                .willReturn(Optional.of(customer));

        // when
        Optional<Customer> optionalCustomer =
                customerService.findByIdAndEmail(customer.getId(), customer.getEmail());

        // then
        assertTrue(optionalCustomer.isPresent());
    }

    @Test
    void testFindValidCustomer() {
        // given
        Customer customer = createCustomer();

        given(customerRepository.findByEmailAndPasswordAndVerifyIsTrue(anyString(), anyString()))
                .willReturn(Optional.of(customer));

        // when
        Optional<Customer> optionalCustomer =
                customerService.findValidCustomer(customer.getEmail(), customer.getPassword());

        // then
        assertTrue(optionalCustomer.isPresent());
    }

}