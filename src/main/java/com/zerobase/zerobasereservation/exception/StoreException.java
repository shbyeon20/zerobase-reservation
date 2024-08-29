package com.zerobase.zerobasereservation.exception;

import com.zerobase.zerobasereservation.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StoreException extends RuntimeException {
    ErrorCode errorCode;
    String message;
}
