package com.vn.fpt.projectcapstoneg4.service;

import com.vn.fpt.projectcapstoneg4.entity.User;
import com.vn.fpt.projectcapstoneg4.model.request.LoginRequest;
import com.vn.fpt.projectcapstoneg4.model.request.SignUpRequest;
import com.vn.fpt.projectcapstoneg4.model.response.LoginResponse;
import com.vn.fpt.projectcapstoneg4.model.response.ResponseAPI;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    public User getUserByEmail(String email);
    public LoginResponse handleLogin(String username, String password);
    public ResponseAPI<Object> signupUser(SignUpRequest request, MultipartFile file);

}
