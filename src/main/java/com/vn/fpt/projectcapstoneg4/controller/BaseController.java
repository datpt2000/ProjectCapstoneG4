package com.vn.fpt.projectcapstoneg4.controller;

import com.vn.fpt.projectcapstoneg4.common.CapstoneUtils;
import com.vn.fpt.projectcapstoneg4.common.CommonConstant;
import com.vn.fpt.projectcapstoneg4.model.bean.UserBean;
import com.vn.fpt.projectcapstoneg4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController {

    @Autowired
    UserService userService;

    protected UserBean getUser(){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return CapstoneUtils.convertUserToBeans(userService.getUserByEmail(auth.getName()));
        }catch (Exception ex){
            throw new RuntimeException(CommonConstant.COMMON_MESSAGE.USER_NOT_LOGIN);
        }
    }

    protected String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
