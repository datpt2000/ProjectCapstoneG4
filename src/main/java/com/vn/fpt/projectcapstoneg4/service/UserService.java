package com.vn.fpt.projectcapstoneg4.service;

import com.vn.fpt.projectcapstoneg4.entity.User;
import com.vn.fpt.projectcapstoneg4.model.bean.UserBean;
import com.vn.fpt.projectcapstoneg4.model.request.LoginRequest;
import com.vn.fpt.projectcapstoneg4.model.request.SignUpRequest;
import com.vn.fpt.projectcapstoneg4.model.request.user.ChangePasswordRequest;
import com.vn.fpt.projectcapstoneg4.model.request.user.DeleteUserRequest;
import com.vn.fpt.projectcapstoneg4.model.response.LoginResponse;
import com.vn.fpt.projectcapstoneg4.model.response.ResponseAPI;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    public User getUserByEmail(String email);
    public LoginResponse handleLogin(String username, String password);
    public ResponseAPI<Object> signupUser(SignUpRequest request, MultipartFile file, String getSiteUrl);
    public ResponseAPI<Object> updateUser(SignUpRequest request, MultipartFile file);
    public ResponseAPI<Object> activeThroughEmail(String verificationCode, String email);
    public ResponseAPI<Object> getListSearch(String name);
    public ResponseAPI<Object> deleteUser(DeleteUserRequest request);
    public ResponseAPI<Object> changePassword(ChangePasswordRequest request, UserBean bean);

    public ResponseAPI<Object> detailUser(long id);



}
