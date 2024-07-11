package com.raving.ebsystem.modular.system.service;

/**
 * 角色相关业务
 */
public interface IRoleService {

    /**
     * 设置某个角色的权限
     *
     * @param roleId 角色id
     * @param ids    权限的id
     */
    void setAuthority(Integer roleId, String ids);

    /**
     * 删除角色
     */
    void delRoleById(Integer roleId);
}
