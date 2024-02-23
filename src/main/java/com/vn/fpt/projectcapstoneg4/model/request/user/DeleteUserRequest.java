package com.vn.fpt.projectcapstoneg4.model.request.user;

import lombok.Data;

@Data
public class DeleteUserRequest {
    private long id;

    public boolean isValid(){
        return id > 0;
    }
}
