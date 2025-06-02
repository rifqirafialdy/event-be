package com.example.event_be.auth.application.impl;

import com.example.event_be.auth.application.services.RbacService;
import com.example.event_be.auth.infrastructure.repositories.SysScreenModeAccessRepository;
import com.example.event_be.auth.infrastructure.repositories.SysUserRoleRepository;
import com.example.event_be.auth.domain.entities.SysUserRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("rbacService")
public class RbacServiceImpl implements RbacService {

    private final SysUserRoleRepository userRoleRepository;
    private final SysScreenModeAccessRepository screenModeAccessRepository;

    public RbacServiceImpl(SysUserRoleRepository userRoleRepository,
                           SysScreenModeAccessRepository screenModeAccessRepository) {
        this.userRoleRepository = userRoleRepository;
        this.screenModeAccessRepository = screenModeAccessRepository;
    }

    @Override
    public boolean hasAccess(String userId, String screenCode, String actionCode) {
        System.out.println("[RBAC DEBUG] hasAccess() called:");
        System.out.println("  userId     = " + userId);
        System.out.println("  screenCode = " + screenCode);
        System.out.println("  actionCode = " + actionCode);

        List<SysUserRole> roles = userRoleRepository.findBySysUserId(userId);
        System.out.println("  found " + roles.size() + " roles");

        List<String> roleCodes = roles.stream()
                .map(role -> role.getSysRole().getCode())
                .toList();
        System.out.println("  roleCodes = " + roleCodes);

        boolean hasAccess = !screenModeAccessRepository
                .findBySysScreenRole_SysRole_CodeInAndSysScreenRole_SysScreen_CodeAndSysScreenAction_SysAction_Code(
                        roleCodes, screenCode, actionCode
                ).isEmpty();

        System.out.println("  final result: " + hasAccess);
        return hasAccess;
    }
}
