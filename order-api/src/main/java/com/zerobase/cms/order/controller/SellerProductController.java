package com.zerobase.cms.order.controller;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.*;
import com.zerobase.cms.order.service.ProductItemService;
import com.zerobase.cms.order.service.ProductService;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller/product")
@RequiredArgsConstructor
public class SellerProductController {

    private final ProductService productService;
    private final ProductItemService productItemService;
    private final JwtAuthenticationProvider provider;

    // product
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                           @RequestBody AddProductForm form) {
        return ResponseEntity.ok()
                .body(ProductDto
                        .from(productService.addProduct(provider.getUserVO(token).getId(), form)));
    }

    @PostMapping("/item")
    public ResponseEntity<ProductDto> addProductItem(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                     @RequestBody AddProductItemForm form) {
        return ResponseEntity.ok()
                .body(ProductDto
                        .from(productItemService.addProductItem(provider.getUserVO(token).getId(), form)));
    }

    // item
    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                 @RequestBody UpdateProductForm form) {
        return ResponseEntity.ok()
                .body(ProductDto
                        .from(productService.updateProduct(provider.getUserVO(token).getId(), form)));
    }

    @PutMapping("/item")
    public ResponseEntity<ProductItemDto> updateProductItem(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                     @RequestBody UpdateProductItemForm form) {
        return ResponseEntity.ok()
                .body(ProductItemDto
                        .from(productItemService.updateProductItem(provider.getUserVO(token).getId(), form)));
    }
}
