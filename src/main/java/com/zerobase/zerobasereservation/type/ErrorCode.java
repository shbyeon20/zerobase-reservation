package com.zerobase.zerobasereservation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    PARTNER_ID_NONEXISTENT("존재하지 않는 partnerID입니다"),
    STORE_ID_NONEXISTENT("존재하지 않는 storeId입니다"),
    USER_ID_NONEXISTENT("존재하지 않는 userId입니다"),
    RESERVATION_ID_NONEXISTENT("존재하지 않는 resservationId입니다"),
    RESERVATION_STATUS_ERROR(" Reservation의 상태가 올바르지 않습니다"),
    REVIEW_NOT_FOUND("존재하지 않는 reviewId입니다"),
    GENERAL_ERROR("dd");

    private final String message;

}
