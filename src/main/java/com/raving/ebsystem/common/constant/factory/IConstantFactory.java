package com.raving.ebsystem.common.constant.factory;

import com.raving.ebsystem.common.constant.cache.Cache;
import com.raving.ebsystem.common.constant.cache.CacheKey;
import com.raving.ebsystem.common.persistence.model.Dict;
import com.raving.ebsystem.common.persistence.model.PollutionType;
import com.raving.ebsystem.common.persistence.model.WaterSource;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 常量生产工厂的接口
 */
public interface IConstantFactory {
    boolean checkDeviceIsExist(Integer deviceId);
    boolean checkPollutionTypeIsExist(Integer pollutionTypeId);
    boolean checkWaterSourceIsExist(Integer waterSourceId);
    String getApplyStatusName(String status);

    String getApplyTypeName(String applyType);
    String getDeviceName(Integer deviceId);
    /**
     * 判断污染值是否超标
     */
    boolean isOverStandard(Double value, Integer pollutionTypeId);


    /**
     * 获取污水类型
     */
    String getWaterTypeName(String waterType);

    /**
     * 根据水源id获取水源名称
     */
    WaterSource getWaterSourceById(Integer waterSourceId);

    /**
     * 根据用户id获取用户名称
     */
    String getUserNameById(Integer userId);

    /**
     * 根据用户id获取用户账号
     */
    String getUserAccountById(Integer userId);

    /**
     * 通过角色ids获取角色名称
     */
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.ROLES_NAME + "'+#roleIds")
    String getRoleName(String roleIds);

    /**
     * 通过角色id获取角色名称
     */
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_NAME + "'+#roleId")
    String getSingleRoleName(Integer roleId);

    /**
     * 通过角色id获取角色英文名称
     */
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_TIP + "'+#roleId")
    String getSingleRoleTip(Integer roleId);

    /**
     * 获取部门名称
     */
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.DEPT_NAME + "'+#deptId")
    String getDeptName(Integer deptId);

    /**
     * 获取菜单的名称们(多个)
     */
    String getMenuNames(String menuIds);

    /**
     * 获取菜单名称
     */
    String getMenuName(Integer menuId);

    /**
     * 获取菜单名称通过编号
     */
    String getMenuNameByCode(String code);

    /**
     * 获取字典名称
     */
    String getDictName(Integer dictId);

    /**
     * 获取通知标题
     */
    String getNoticeTitle(Integer dictId);

    /**
     * 根据字典名称和字典中的值获取对应的名称
     */
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.DICT_NAME + "'+#name+'_'+#val")
    String getDictsByName(String name, Integer val);

    /**
     * 获取性别名称
     */
    String getSexName(Integer sex);

    /**
     * 获取用户登录状态
     */
    String getStatusName(Integer status);

    /**
     * 获取菜单状态
     */
    String getMenuStatusName(Integer status);

    /**
     * 查询字典
     */
    List<Dict> findInDict(Integer id);

    /**
     * 获取被缓存的对象(用户删除业务)
     */
    String getCacheObject(String para);

    PollutionType getPollutionTypeById(Integer pollutionTypeId);
    /**
     * 获取子部门id
     */
    List<Integer> getSubDeptId(Integer deptid);

    /**
     * 获取所有父部门id
     */
    List<Integer> getParentDeptIds(Integer deptid);

    String getDateTime(Long updateTime);
}
