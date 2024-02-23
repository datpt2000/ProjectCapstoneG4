package com.vn.fpt.projectcapstoneg4.model.bean;

import lombok.Data;

import java.util.Date;

@Data
public class UserBean {
    private long id;
    private String email;
    private String authority;
    private String fullname;
    private String deleteFlg;
    private String department;
    private Boolean isActive;
    private Date createTime;
}
