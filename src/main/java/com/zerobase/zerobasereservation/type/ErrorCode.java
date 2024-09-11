package com.zerobase.zerobasereservation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    GENERAL_ERROR("올바르지 않은 요청입니다"),
    PARTNERID_NONEXISTENT("존재하지 않는 partnerID입니다"),
    STOREID_NONEXISTENT("존재하지 않는 storeId입니다"),
    USERID_NONEXISTENT("존재하지 않는 userId입니다"),
    RESERVATION_ID_NONEXISTENT("존재하지 않는 resservationId입니다"),
    RESERVATION_STATUS_ERROR(" Reservation의 상태가 올바르지 않습니다"),
    REVIEW_NOT_FOUND("존재하지 않는 reviewId입니다"),
    PASSWORD_UNMATCHED("비밀번호가 일치하지 않습니다"),
    CONFIRMATION_TOO_LATE("예약확정은 예약시간이 되기 10분 이전까지만 가능합니다"),
    USERID_REVIEWUSER_UNMATCHED("Review의 User와 UserID가 일치하지 않습니다."),
    MEMBERID_REVIEWUSER_UNMATCHED("Review의 Member와 MemberId가 일치하지 않습니다."),
    MEMBERID_STOREOWNER_UNMATCHED("store의 Owner와 MemberId가 일치하지 않습니다.");




    private final String message;

}
