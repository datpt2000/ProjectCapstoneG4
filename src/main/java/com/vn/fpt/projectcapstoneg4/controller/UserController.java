package com.vn.fpt.projectcapstoneg4.controller;

import com.vn.fpt.projectcapstoneg4.common.CommonConstant;
import com.vn.fpt.projectcapstoneg4.model.request.LoginRequest;
import com.vn.fpt.projectcapstoneg4.model.request.SignUpRequest;
import com.vn.fpt.projectcapstoneg4.model.response.ResponseAPI;
import com.vn.fpt.projectcapstoneg4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @PostMapping(CommonConstant.USER_API.LOGIN)
    public ResponseAPI<Object> login(@RequestBody LoginRequest request) {
        ResponseAPI<Object> result = new ResponseAPI<Object>();
        if(request.isValid()){
            try {
                result.setStatus(CommonConstant.COMMON_RESPONSE.OK);
                result.setMessage(CommonConstant.COMMON_MESSAGE.OK);
                result.setData(userService.handleLogin(request.getEmail(), request.getPassword()));
            } catch (BadCredentialsException ex) {
                result.setStatus(CommonConstant.COMMON_RESPONSE.EXCEPTION);
                result.setMessage(CommonConstant.COMMON_MESSAGE.PASSWORD_INCORRECT);
            } catch (NullPointerException ex) {
                result.setStatus(CommonConstant.COMMON_RESPONSE.EXCEPTION);
                result.setMessage(CommonConstant.COMMON_MESSAGE.INVALID_PARAMETER);
            } catch (Exception ex) {
                result.setStatus(CommonConstant.COMMON_RESPONSE.EXCEPTION);
                result.setMessage(ex.getMessage());
            }
            return result;
        }else {
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.INVALID_PARAMETER);
        }
    }

    @PostMapping(value = CommonConstant.USER_API.SIGN_UP,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseAPI<Object> signup(@RequestPart("request") SignUpRequest request , @RequestPart(value = "file", required = false) MultipartFile file) {
        ResponseAPI<Object> result = new ResponseAPI<Object>();
        if(request.isValid()){
          return userService.signupUser(request, file);
        }
        return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.INVALID_PARAMETER);

    }

}
