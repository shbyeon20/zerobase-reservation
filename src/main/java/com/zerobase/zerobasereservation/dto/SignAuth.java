package com.zerobase.zerobasereservation.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


public class SignAuth {
    @Getter
    @Setter
    public static class SignIn{
        @Size(min=5, max=30)
        private String Id;
        @Size(min=5, max=30)
        private String Password;
    }

    @Getter
    @Setter
    public static class SignUp{
        @Size(min=2, max=30)
        private String Id;
        @Size(min=5, max=30)
        private String Password;
    }
}
