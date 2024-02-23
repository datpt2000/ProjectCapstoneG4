package com.vn.fpt.projectcapstoneg4.service.impl;

import com.vn.fpt.projectcapstoneg4.common.CapstoneUtils;
import com.vn.fpt.projectcapstoneg4.common.CommonConstant;
import com.vn.fpt.projectcapstoneg4.config.CustomerUserDetails;
import com.vn.fpt.projectcapstoneg4.config.SecurityWebConfig;
import com.vn.fpt.projectcapstoneg4.config.TokenProvider;
import com.vn.fpt.projectcapstoneg4.entity.User;
import com.vn.fpt.projectcapstoneg4.model.bean.UserBean;
import com.vn.fpt.projectcapstoneg4.model.request.SignUpRequest;
import com.vn.fpt.projectcapstoneg4.model.request.user.DeleteUserRequest;
import com.vn.fpt.projectcapstoneg4.model.response.LoginResponse;
import com.vn.fpt.projectcapstoneg4.model.response.ResponseAPI;
import com.vn.fpt.projectcapstoneg4.repository.UserRepository;
import com.vn.fpt.projectcapstoneg4.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    SecurityWebConfig securityConfig;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.sender.display-name}")
    private String displayNameEmail;


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public User getUserByEmail(String email) {
        try {
            User result = userRepository.findByEmailAndDeleteFlg(email, "0");
            if (result == null || null == result.getEmail()) {
                return null;
            } else {
                return result;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public LoginResponse handleLogin(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            // Nếu không xảy ra exception tức là thông tin hợp lệ
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomerUserDetails authenUser = (CustomerUserDetails) authentication.getPrincipal();
            User user = this.getUserByEmail(authenUser.getUsername());
            // Trả về jwt cho người dùng.
            String jwt = tokenProvider.generateJwtToken(user.getEmail());
            String role = user.getAuthority();
            return new LoginResponse(email, user.getId(), jwt, role);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public ResponseAPI<Object> signupUser(SignUpRequest request, MultipartFile file, String getSiteUrl) {
        try {
            if (userRepository.existsByEmail(request.email)) {
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, "EMAIL EXISTED");
            } else {
                User userEntity = new User();
                if (file.getSize() > 0) {
                    userEntity.setImageUrl(handleAvatar(file));
                }
                userEntity.setEmail(request.getEmail());
                userEntity.setFirstName(request.getFirstName());
                userEntity.setLastName(request.getLastName());
                userEntity.setAuthority(request.getAuthority());
                userEntity.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(request.getDateOfBirth()));
                userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
                userEntity.setIsActive(false);
                userEntity.setDeleteFlg("0");
                userEntity.setCreateTime(new Date());
                userEntity.setGender(request.getGender());
                userEntity.setAddress(request.getAddress());
                String resetCode = RandomString.make(64);
                userEntity.setVerificationCode(resetCode);
                userRepository.saveAndFlush(userEntity);

                //sendMail
                activeCreateUser(request, resetCode, getSiteUrl);
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.OK, "OK");
            }
        } catch (Exception e) {
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.EXCEPTION, CommonConstant.COMMON_MESSAGE.EXCEPTION);
        }

    }

    @Override
    public ResponseAPI<Object> activeThroughEmail(String verificationCode, String email) {
        try {
            User userActive = userRepository.findByEmailAndVerificationCodeAndDeleteFlg(email, verificationCode, "0");
            if (null != userActive) {
                userActive.setIsActive(true); //active có quyền truy cập
                userActive.setVerificationCode(null);
                userRepository.saveAndFlush(userActive);
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.OK, "OK");
            }
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, "INVALID_EMAIL_OR_TOKEN");

        } catch (Exception e) {
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.EXCEPTION, CommonConstant.COMMON_MESSAGE.EXCEPTION);
        }
    }

    @Override
    public ResponseAPI<Object> getListSearch(String name) {
        try {
            if (null == name) {
                name = "";
            }
            List<User> getListSearch = userRepository.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndDeleteFlg(name, name, "0");
            if (null != getListSearch && getListSearch.size() > 0) {
                List<UserBean> listResponse = new ArrayList<>();
                for (User user : getListSearch) {
                    listResponse.add(CapstoneUtils.convertUserToBeans(user));
                }
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.OK, CommonConstant.COMMON_MESSAGE.OK, listResponse);
            }
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.EMPTY);
        } catch (Exception e) {
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.EXCEPTION, CommonConstant.COMMON_MESSAGE.EXCEPTION);
        }

    }

    @Override
    public ResponseAPI<Object> deleteUser(DeleteUserRequest request) {
        try {
            User deleteUser = userRepository.findByIdAndDeleteFlg(request.getId(), "0");
            if (null != deleteUser) {
                deleteUser.setDeleteFlg("1"); //1 là bị xóa mềm, 0 là chưa xóa
                userRepository.saveAndFlush(deleteUser);
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.OK, CommonConstant.COMMON_MESSAGE.OK);
            }
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, "ERROR");

        } catch (Exception e) {
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.EXCEPTION, e.getMessage());
        }

    }

    public String handleAvatar(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }

    public void activeCreateUser(SignUpRequest request, String verifyCode, String siteURL) {
        try {
            String toAddress = request.getEmail();
            String fromAddress = fromEmail;
            String senderName = "FPT Education";
            String subject = "Please verify your registration";
            String content = "Dear [[name]],<br>"
                    + "\t Password : [[password]]<br>"
                    + "Please click the link below and enter code to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Thank you,<br>"
                    + "FPT Education.";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            content = content.replace("[[name]]", request.getFirstName() + " " + request.getLastName());
            content = content.replace("[[password]]", request.getPassword());
            String verifyURL = siteURL + "/api/user/active-through-email?token=" + verifyCode + "&email=" + request.getEmail();
            content = content.replace("[[URL]]", verifyURL);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause());
        }
    }
}
