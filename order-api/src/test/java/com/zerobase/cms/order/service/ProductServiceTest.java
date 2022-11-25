package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    void testAddProduct() {
        // given
        Long sellerId = 1L;
        AddProductForm form = makeProductForm("나이키 에어포스", "신발입니다.", 3);

        // when
        Product p = productService.addProduct(sellerId, form);
        Product savedProduct = productRepository.findById(p.getId()).get();

        // then
        assertNotNull(savedProduct);
        assertEquals(savedProduct.getId(), p.getId());
        assertEquals(savedProduct.getName(), "나이키 에어포스");
        assertEquals(savedProduct.getDescription(), "신발입니다.");
        assertEquals(savedProduct.getSellerId(), 1L);
        assertEquals(savedProduct.getProductItems().size(), 3);
        assertEquals(savedProduct.getProductItems().get(0).getName(), "나이키 에어포스0");
        assertEquals(savedProduct.getProductItems().get(0).getPrice(), 10000);
        assertEquals(savedProduct.getProductItems().get(0).getCount(), 1);
    }

    private static AddProductForm makeProductForm(String name, String description, int itemCnt) {
        List<AddProductItemForm> itemForms = new ArrayList<>();
        for (int i = 0;i < itemCnt;i++) {
            itemForms.add(makeProductItemForm(null, name + i));
        }
        return AddProductForm.builder()
                .name(name)
                .description(description)
                .items(itemForms)
                .build();
    }

    private static AddProductItemForm makeProductItemForm(Long productId, String name) {
        return AddProductItemForm.builder()
                .productId(productId)
                .name(name)
                .price(10000)
                .count(1)
                .build();
    }
}