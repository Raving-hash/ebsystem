package com.raving.ebsystem.common.persistence.dao;

import com.raving.ebsystem.common.persistence.model.Role;
import com.raving.ebsystem.common.node.ZTreeNode;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 角色表 Mapper 接口
 * </p>
 * @since 2017-07-11
 */
public interface RoleMapper extends Mapper<Role> {

    /**
     * 根据条件查询角色列表
     *
     * @return
     */
    List<Map<String, Object>> selectRoles(@Param("condition") String condition);

    /**
     * 删除某个角色的所有权限
     *
     * @param roleId 角色id
     * @return
     */
    int deleteRolesById(@Param("roleId") Integer roleId);

    /**
     * 获取角色列表树
     *
     * @return
     */
    List<ZTreeNode> roleTreeList();

    /**
     * 获取角色列表树
     *
     * @return
     */
    List<ZTreeNode> roleTreeListByRoleId(String[] roleId);

}