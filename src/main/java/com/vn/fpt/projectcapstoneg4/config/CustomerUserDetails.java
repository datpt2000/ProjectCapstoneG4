package com.vn.fpt.projectcapstoneg4.config;

import com.vn.fpt.projectcapstoneg4.common.CommonConstant;
import com.vn.fpt.projectcapstoneg4.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//config sql gradle
@Data
@NoArgsConstructor
public class CustomerUserDetails implements org.springframework.security.core.userdetails.UserDetails{

    private Long id;
    private String email;
    private String password;
    private String fullName;
    private Collection<? extends GrantedAuthority> authorities;

    private boolean isActive;

    public CustomerUserDetails(Long id, String fullName, String password, String email,
                               Collection<? extends GrantedAuthority> authorities, boolean isActive) {
        this.id = id;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.isActive = isActive;
    }
    public static CustomerUserDetails build(User userEntity) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(CommonConstant.COMMON_ROLE.ROLE + userEntity.getAuthority()));

        return new CustomerUserDetails(
                userEntity.getId(),
                returnFullName(userEntity.firstName, userEntity.lastName),
                userEntity.getPassword(),
                userEntity.getEmail(),
                authorities,
                userEntity.getIsActive()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used to authenticate the user. Cannot return <code>null</code>.
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static String returnFullName(String firstName, String lastName){
        return firstName + " " + lastName;
    }



}
