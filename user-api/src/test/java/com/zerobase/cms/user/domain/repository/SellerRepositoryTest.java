package com.zerobase.cms.user.domain.repository;

import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class SellerRepositoryTest {

    @Autowired
    private SellerRepository sellerRepository;

    private Seller saveSeller() {
        Seller seller = Seller.builder()
                .email("test@test.com")
                .name("test")
                .phone("010-0000-0000")
                .birth(LocalDate.now())
                .password("test")
                .verify(true)
                .build();
        sellerRepository.save(seller);

        return seller;
    }

    @Test
    void testFindByIdAndEmail() {
        // given
        Seller savedSeller = saveSeller();
        Long id = savedSeller.getId();
        String email = savedSeller.getEmail();

        // when
        Optional<Seller> optionalSeller = sellerRepository.findByIdAndEmail(id, email);

        // then
        assertTrue(optionalSeller.isPresent());
    }

    @Test
    void testFindByEmailAndPasswordAndVerifyIsTrue() {
        // given
        Seller savedSeller = saveSeller();
        String email = savedSeller.getEmail();
        String password = savedSeller.getPassword();

        // when
        Optional<Seller> optionalSeller =
                sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email, password);

        // then
        assertTrue(optionalSeller.isPresent());
    }

    @Test
    void testFindByEmail() {
        // given
        Seller savedSeller = saveSeller();
        String email = savedSeller.getEmail();

        // when
        Optional<Seller> optionalSeller = sellerRepository.findByEmail(email);

        // then
        assertTrue(optionalSeller.isPresent());
    }
}