package com.vn.fpt.projectcapstoneg4.service.impl;

import com.vn.fpt.projectcapstoneg4.common.CommonConstant;
import com.vn.fpt.projectcapstoneg4.config.CustomerUserDetails;
import com.vn.fpt.projectcapstoneg4.config.SecurityWebConfig;
import com.vn.fpt.projectcapstoneg4.config.TokenProvider;
import com.vn.fpt.projectcapstoneg4.entity.User;
import com.vn.fpt.projectcapstoneg4.model.request.SignUpRequest;
import com.vn.fpt.projectcapstoneg4.model.response.LoginResponse;
import com.vn.fpt.projectcapstoneg4.model.response.ResponseAPI;
import com.vn.fpt.projectcapstoneg4.repository.UserRepository;
import com.vn.fpt.projectcapstoneg4.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

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
    public ResponseAPI<Object> signupUser(SignUpRequest request, MultipartFile file) {
        try{
            if (userRepository.existsByEmail(request.email)) {
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, "EMAIL EXISTED");
            } else {
                User userEntity = new User();
                if(file.getSize() > 0){
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
                userRepository.saveAndFlush(userEntity);
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.OK, "OK");
            }
        }catch (Exception e){
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.EXCEPTION, CommonConstant.COMMON_MESSAGE.EXCEPTION);
        }

    }

    public String handleAvatar(MultipartFile file) throws IOException {
        return  Base64.getEncoder().encodeToString(file.getBytes());
    }
}
