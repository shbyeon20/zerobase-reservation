package com.zerobase.zerobasereservation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


public class SignAuth {
    @Getter
    @Setter
    public static class SignIn{
        @NotNull
        @Size(min=5, max=30)
        private String id;
        @NotNull
        @Size(min=5, max=30)
        private String password;
    }

    @Getter
    @Setter
    public static class SignUp{
        @NotNull
        @Size(min=2, max=30)
        private String Id;
        @NotNull
        @Size(min=5, max=30)
        private String Password;
    }
}
