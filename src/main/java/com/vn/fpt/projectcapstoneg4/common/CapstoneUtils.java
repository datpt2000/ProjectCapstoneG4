package com.vn.fpt.projectcapstoneg4.common;

import com.vn.fpt.projectcapstoneg4.entity.User;
import com.vn.fpt.projectcapstoneg4.model.bean.UserBean;

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


        return userBeans;
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
