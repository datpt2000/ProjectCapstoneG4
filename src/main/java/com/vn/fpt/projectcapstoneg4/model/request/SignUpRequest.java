package com.vn.fpt.projectcapstoneg4.model.request;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class SignUpRequest {

    private long id;
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String address;

    private String gender;

    private String dateOfBirth;

    private String authority;


    public boolean isValid(){
        return StringUtils.hasLength(email)
                && StringUtils.hasLength(firstName)
                && StringUtils.hasLength(lastName)
                && null != dateOfBirth
                && StringUtils.hasLength(authority)
                && StringUtils.hasLength(address)
                && StringUtils.hasLength(password)
                && StringUtils.hasLength(gender);

    }

    public boolean isValidUpdate() {
        return  id > 0
                && StringUtils.hasLength(firstName)
                && StringUtils.hasLength(lastName)
                && null != dateOfBirth
                && StringUtils.hasLength(authority)
                && StringUtils.hasLength(address)
                && StringUtils.hasLength(gender);
    }
}
