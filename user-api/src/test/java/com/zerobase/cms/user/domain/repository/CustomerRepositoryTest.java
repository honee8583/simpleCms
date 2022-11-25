package com.zerobase.cms.user.domain.repository;

import com.zerobase.cms.user.domain.model.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer saveCustomer() {
        Customer customer = Customer.builder()
                .email("test@test.com")
                .name("test")
                .phone("010-0000-0000")
                .birth(LocalDate.now())
                .password("test")
                .build();
        customerRepository.save(customer);

        return customer;
    }

    @Test
    void testFindByEmail() {
        // given
        Customer savedCustomer = saveCustomer();
        String email = savedCustomer.getEmail();

        // when
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);

        // then
        assertTrue(optionalCustomer.isPresent());
    }
}