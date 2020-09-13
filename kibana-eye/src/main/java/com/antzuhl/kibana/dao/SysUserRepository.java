package com.antzuhl.kibana.dao;

import com.antzuhl.kibana.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author AntzUhl
 * @Date 14:08
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findSysUserByUsername(String username);
}
