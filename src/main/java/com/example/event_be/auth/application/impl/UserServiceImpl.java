package com.example.event_be.auth.application.impl;

import com.example.event_be.auth.application.services.UserService;
import com.example.event_be.auth.domain.entities.SysUser;

import com.example.event_be.auth.infrastructure.repositories.SysUserRepository;
import com.example.event_be.auth.infrastructure.repositories.SysUserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserRepository sysUserRepository;
    private final SysUserRoleRepository sysUserRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SysUser user = sysUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<SimpleGrantedAuthority> roles = sysUserRoleRepository.findBySysUserId(user.getId())
                .stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getSysRole().getCode())) // e.g. ROLE_CUSTOMER
                .toList();

        return User.builder()
                .username(user.getId()) // ✅ use ID so authentication.getName() = userId
                .password(user.getPassword())
                .authorities(roles)
                .build();

    }
}
