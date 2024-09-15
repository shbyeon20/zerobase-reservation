package com.zerobase.zerobasereservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class ZerobaseReservationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZerobaseReservationApplication.class, args);
    }
}
