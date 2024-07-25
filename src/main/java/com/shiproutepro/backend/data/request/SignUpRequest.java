package com.shiproutepro.backend.data.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {
    private String companyName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String companyAddress;
}
