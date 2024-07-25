package com.shiproutepro.backend.entities;

import com.shiproutepro.backend.enums.AccountCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String companyName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String companyAddress;

    @Column(name = "account_type")
    private AccountCategory type;

}
