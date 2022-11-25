package com.zerobase.cms.user.service.customer;

import com.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.CustomerBalanceHistory;
import com.zerobase.cms.user.domain.repository.CustomerBalanceHistoryRepository;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerBalanceHistoryService {

    private final CustomerBalanceHistoryRepository historyRepository;
    private final CustomerRepository customerRepository;

    @Transactional(noRollbackFor = {CustomException.class})
    public CustomerBalanceHistory changeBalance(Long customerId, ChangeBalanceForm form)
            throws CustomException{
        // 가장 최근 내역 1개 조회(존재하지 않을 시 초기값으로 반환)
        CustomerBalanceHistory customerBalanceHistory =
                historyRepository.findFirstByCustomer_IdOrderByIdDesc(customerId)
                        .orElse(CustomerBalanceHistory.builder()
                                .changeMoney(0)
                                .currentMoney(0)
                                .customer(customerRepository.findById(customerId)
                                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)))
                                .build());

        // 잔액보다 사용량이 더 많을 경우
        if (customerBalanceHistory.getCurrentMoney() + form.getMoney() < 0) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_BALANCE);
        }

        //
        customerBalanceHistory = CustomerBalanceHistory.builder()
                                    .changeMoney(form.getMoney())
                                    .currentMoney(customerBalanceHistory.getCurrentMoney() + form.getMoney())
                                    .description(form.getMessage())
                                    .fromMessage(form.getFrom())
                                    .customer(customerBalanceHistory.getCustomer())
                                    .build();

        // 사용자의 잔액 변경
        customerBalanceHistory.getCustomer()
                .changeBalance(customerBalanceHistory.getChangeMoney());

        return historyRepository.save(customerBalanceHistory);
    }

}
