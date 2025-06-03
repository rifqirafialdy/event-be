package com.example.event_be.auth.infrastructure.repositories;

import com.example.event_be.auth.domain.entities.SysScreenModeAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysScreenModeAccessRepository extends JpaRepository<SysScreenModeAccess, String> {

    List<SysScreenModeAccess> findBySysScreenRole_SysRole_CodeInAndSysScreenRole_SysScreen_CodeAndSysScreenAction_SysAction_Code(
            List<String> roleCodes,
            String screenCode,
            String actionCode
    );
    List<SysScreenModeAccess> findBySysScreenRole_SysRole_CodeIn(List<String> roleCodes);

}
