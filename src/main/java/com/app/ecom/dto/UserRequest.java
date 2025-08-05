package com.app.ecom.dto;

import com.app.ecom.model.Address;
import com.app.ecom.model.UserRole;

import lombok.Data;

@Data
public class UserRequest
{
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private UserRole role;
    private Address address;
}
