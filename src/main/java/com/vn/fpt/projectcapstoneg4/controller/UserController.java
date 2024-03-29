package com.vn.fpt.projectcapstoneg4.controller;

import com.vn.fpt.projectcapstoneg4.common.CommonConstant;
import com.vn.fpt.projectcapstoneg4.model.bean.UserBean;
import com.vn.fpt.projectcapstoneg4.model.request.LoginRequest;
import com.vn.fpt.projectcapstoneg4.model.request.SignUpRequest;
import com.vn.fpt.projectcapstoneg4.model.request.user.ChangePasswordRequest;
import com.vn.fpt.projectcapstoneg4.model.request.user.DeleteUserRequest;
import com.vn.fpt.projectcapstoneg4.model.response.ResponseAPI;
import com.vn.fpt.projectcapstoneg4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @PostMapping(CommonConstant.USER_API.LOGIN)
    public ResponseAPI<Object> login(@RequestBody LoginRequest request) {
        ResponseAPI<Object> result = new ResponseAPI<Object>();
        if (request.isValid()) {
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
        } else {
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.INVALID_PARAMETER);
        }
    }

    @PostMapping(value = CommonConstant.USER_API.SIGN_UP,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseAPI<Object> signup(HttpServletRequest requestHttp, @RequestPart("request") SignUpRequest request, @RequestPart(value = "file", required = false) MultipartFile file) {
        if (request.isValid()) {
            return userService.signupUser(request, file, getSiteURL(requestHttp));
        }
        return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.INVALID_PARAMETER);

    }

    @PostMapping(value = CommonConstant.USER_API.UPDATE_USER,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseAPI<Object> updateUser(@RequestPart("request") SignUpRequest request, @RequestPart(value = "file", required = false) MultipartFile file) {
        if (request.isValidUpdate()) {
            return userService.updateUser(request, file);
        }
        return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.INVALID_PARAMETER);

    }

    @GetMapping(value = CommonConstant.USER_API.ACTIVE_USER)
    public ResponseAPI<Object> activeUser(@RequestParam("email") String email, @RequestParam(value = "token") String verifyCode) {
        if (StringUtils.hasLength(email) && StringUtils.hasLength(verifyCode)) {
            return userService.activeThroughEmail(verifyCode, email);
        }
        return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.INVALID_PARAMETER);

    }

    @GetMapping(value = CommonConstant.USER_API.GET_LIST_USER)
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseAPI<Object> searchListUser(@RequestParam(value = "name", required = false) String name) {
        return userService.getListSearch(name);
    }

    @PostMapping(value = CommonConstant.USER_API.DELETE_USER)
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseAPI<Object> deleteUser(@RequestBody DeleteUserRequest request) {
        return userService.deleteUser(request);
    }

    @PostMapping(value = CommonConstant.USER_API.CHANGE_PASSWORD)
    public ResponseAPI<Object> changePassword(@RequestBody ChangePasswordRequest request) {
        UserBean bean = getUser();
        if (null != bean) {
            if (StringUtils.hasLength(request.getPassword())) {
                return userService.changePassword(request, bean);
            }
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.INVALID_PARAMETER);
        }
        return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NON_AUTH, CommonConstant.COMMON_MESSAGE.USER_NOT_LOGIN);
    }

    @GetMapping(value = CommonConstant.USER_API.DETAIL_USER)
    public ResponseAPI<Object> detailUser(@RequestParam(value = "id") long id) {
        if (id > 0) {
            return userService.detailUser(id);
        }
        return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.INVALID_PARAMETER);
    }


}
