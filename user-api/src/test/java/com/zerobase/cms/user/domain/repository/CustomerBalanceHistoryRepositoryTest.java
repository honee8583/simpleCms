package com.zerobase.cms.user.domain.repository;

import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.CustomerBalanceHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class CustomerBalanceHistoryRepositoryTest {

    @Autowired
    private CustomerBalanceHistoryRepository historyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerBalanceHistory saveCustomerBalanceHistory() {
        Customer savedCustomer = customerRepository.save(
                Customer.builder().email("test@test.com").build()
        );

        CustomerBalanceHistory history = CustomerBalanceHistory.builder()
                .customer(savedCustomer)
                .changeMoney(1000)
                .currentMoney(10000)
                .fromMessage("message")
                .description("description")
                .build();

        return historyRepository.save(history);
    }

    @Test
    void testFindFirstByCustomer_IdOrderByIdDesc() {
        // given
        CustomerBalanceHistory savedHistory = saveCustomerBalanceHistory();
        Long customerId = savedHistory.getCustomer().getId();

        // when
        Optional<CustomerBalanceHistory> optionalHistory =
                historyRepository.findFirstByCustomer_IdOrderByIdDesc(customerId);

        // then
        assertTrue(optionalHistory.isPresent());
    }
}