package com.vn.fpt.projectcapstoneg4.model.request;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class SignUpRequest {

    public String email;

    private String password;


    public String firstName;

    public String lastName;

    private String address;

    public String gender;

    public String dateOfBirth;

    private String authority;


    public boolean isValid(){
        return StringUtils.hasLength(email)
                && StringUtils.hasLength(firstName)
                && StringUtils.hasLength(lastName)
                && null != dateOfBirth
                && StringUtils.hasLength(authority)
                && StringUtils.hasLength(address)
                && StringUtils.hasLength(password);

    }
}
