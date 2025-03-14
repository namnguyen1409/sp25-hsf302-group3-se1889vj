package com.group3.sp25hsf302group3se1889vj.security.userdetails;

import com.group3.sp25hsf302group3se1889vj.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record CustomUserDetails(User user) implements UserDetails {

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        user.getPermissions().forEach(
                permission -> authorities.add(new SimpleGrantedAuthority(permission.getName().name()))
        );
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.isDeleted();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
