package com.vn.fpt.projectcapstoneg4.model.request.player;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class CreatePlayerRequest {
    private long id;
    private String name;
    private String dateOfBirth;
    private Integer height;
    private Integer weight;
    private String nationality;
    private String position;
    private String bio;
    private String joinDate;

    public boolean isValid(){
        return StringUtils.hasLength(name)
                &&  null != weight && weight > 0
                && null != height && height > 0
                && null != dateOfBirth
                && StringUtils.hasLength(nationality)
                && StringUtils.hasLength(position)
                && null != joinDate;
    }

    public boolean isValidUpdate(){
        return id > 0 && isValid();
    }
}
