package com.zerobase.cms.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
    ALREADY_EXIST_ITEM(HttpStatus.BAD_REQUEST, "아이템 명 중복입니다.");

    private final HttpStatus status;
    private final String detail;
}