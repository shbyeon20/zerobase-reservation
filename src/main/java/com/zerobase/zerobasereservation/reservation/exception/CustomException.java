package com.zerobase.zerobasereservation.reservation.exception;

import com.zerobase.zerobasereservation.reservation.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomException extends RuntimeException {
    ErrorCode errorCode;
    String message;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
