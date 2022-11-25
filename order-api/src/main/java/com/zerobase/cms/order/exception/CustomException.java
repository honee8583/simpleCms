package com.zerobase.cms.order.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private ErrorCode errorCode;
    private int status;
    private static final ObjectMapper mapper = new ObjectMapper();

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
        this.status = errorCode.getStatus().value();
    }
}
