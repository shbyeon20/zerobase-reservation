package com.zerobase.zerobasereservation.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class CreateStore {


    @Getter @Setter
    @AllArgsConstructor
    public static class Request {

        @Size(min=1,max=10)
        private String partnerId;
        @Size(min=1,max=10)
        private String storeId;

        @NotNull
        private String address;
        private String storeComment;


    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String partnerId;
        private String storeId;
        private String address;
        private String storeComment;


        public static Response fromDto(StoreDto storeDto) {
            return Response.builder()
                    .partnerId(storeDto.getPartnerId())
                    .storeId(storeDto.getStoreId())
                    .address(storeDto.getAddress())
                    .storeComment(storeDto.getStoreComment())
                    .build();
        }
    }
}
