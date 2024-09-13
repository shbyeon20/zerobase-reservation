package com.zerobase.zerobasereservation.exception;

import com.zerobase.zerobasereservation.type.ErrorCode;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ErrorResponse {

    ErrorCode errorCode;
    private String message;
    private List<String> details = new ArrayList<>();


}
