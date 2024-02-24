package com.vn.fpt.projectcapstoneg4.common;

import com.vn.fpt.projectcapstoneg4.entity.User;
import com.vn.fpt.projectcapstoneg4.model.bean.UserBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class CapstoneUtils {

    public static UserBean convertUserToBeans(User user) {
        if (null == user) {
            return null;
        }
        UserBean userBeans = new UserBean();
        userBeans.setId(user.getId());
        if (null != user.getEmail()) {
            userBeans.setEmail(user.getEmail());
        }

        if (null != user.getDeleteFlg()) {
            userBeans.setDeleteFlg(user.getDeleteFlg());
        }
        if (null != user.getAuthority()) {
            userBeans.setAuthority(user.getAuthority());
        }
        if (null != user.getEmail()) {
            userBeans.setEmail(user.getEmail());
        }
        if (null != user.getFirstName() &&  null != user.getLastName()) {
            userBeans.setFullname(user.getFirstName() + " " + user.getLastName());
        }
        if (null != user.getCreateTime()) {
            userBeans.setCreateTime(user.getCreateTime());
        }

        if(null != user.getGender()){
            userBeans.setGender(user.getGender());
        }

        if(null != user.getIsActive()){
            userBeans.setIsActive(user.getIsActive());
        }

        if(null != user.getAddress()){
            userBeans.setAddress(user.getAddress());
        }

        if(null != user.getImageUrl()){
            userBeans.setImageUrl(user.getImageUrl());
        }

        return userBeans;
    }

    public static String handleAvatar(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }
//    public static String readSqlFile(String filename) {
//        ClassLoader classLoader = org.apache.commons.lang3.StringUtils.class.getClassLoader();
//        File file = new File(classLoader.getResource(filename).getFile());
//        String content = CommonConstant.COMMON_STRING.BLANK;
//        try {
//            content = new String(Files.readAllBytes(Paths.get(file.getPath())));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return content;
//    }
}
