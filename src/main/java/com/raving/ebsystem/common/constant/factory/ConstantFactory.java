package com.raving.ebsystem.common.constant.factory;

import com.raving.ebsystem.common.constant.state.ManagerStatus;
import com.raving.ebsystem.common.constant.state.MenuStatus;
import com.raving.ebsystem.common.persistence.dao.*;
import com.raving.ebsystem.common.persistence.model.*;
import com.raving.ebsystem.core.log.LogObjectHolder;
import com.raving.ebsystem.core.support.StrKit;
import com.raving.ebsystem.core.util.Convert;
import com.raving.ebsystem.core.util.DateUtil;
import com.raving.ebsystem.core.util.SpringContextHolder;
import com.raving.ebsystem.core.util.ToolUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 常量的生产工厂
 */
@Component
@DependsOn("springContextHolder")
@CacheConfig(cacheNames = "caffeineCacheManager")
public class ConstantFactory implements IConstantFactory {

    private RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
    private DeptMapper deptMapper = SpringContextHolder.getBean(DeptMapper.class);
    private DictMapper dictMapper = SpringContextHolder.getBean(DictMapper.class);
    private UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private MenuMapper menuMapper = SpringContextHolder.getBean(MenuMapper.class);
    private NoticeMapper noticeMapper = SpringContextHolder.getBean(NoticeMapper.class);
    private PollutionMapper pollutionMapper = SpringContextHolder.getBean(PollutionMapper.class);
    private PollutionTypeMapper pollutionTypeMapper = SpringContextHolder.getBean(PollutionTypeMapper.class);
    private WaterSourceMapper waterSourceMapper = SpringContextHolder.getBean(WaterSourceMapper.class);
    private DeviceMapper deviceMapper = SpringContextHolder.getBean(DeviceMapper.class);
    private ApplicationMapper applicationMapper = SpringContextHolder.getBean(ApplicationMapper.class);
    private WarnWaterMapper warnWaterMapper = SpringContextHolder.getBean(WarnWaterMapper.class);
    private static Map<String, String> waterTypeDict = new HashMap<>();
    private static Map<String, String> applyTypeDict = new HashMap<>();
    private static Map<String, String> applyStatusDict = new HashMap<>();

    static {
        initWaterTypeDict();
        initApplyTypeDict();
        initApplyStatusDict();
    }

    public static void initWaterTypeDict() {
        waterTypeDict.put("0", "工业污水");
        waterTypeDict.put("1", "生活污水");
    }

    public static void initApplyTypeDict() {
        applyTypeDict.put("0", "设备申请");
        applyTypeDict.put("1", "设备维护");
        applyTypeDict.put("2", "设备归还");
    }

    public static void initApplyStatusDict() {
        applyStatusDict.put("0", "待审核");
        applyStatusDict.put("1", "审核通过");
        applyStatusDict.put("2", "审核不通过");
    }

    public static IConstantFactory me() {
        return SpringContextHolder.getBean("constantFactory");
    }

    @Cacheable(key = "#pollutionTypeId + 'pollutionType'")
    public PollutionType getPollutionTypeById(Integer pollutionTypeId) {
        PollutionType pollutionType = pollutionTypeMapper.getPollutionTypeById(pollutionTypeId);
        if (pollutionType != null) {
            return pollutionType;
        } else {
            return null;
        }
    }

    @Override
    public boolean checkDeviceIsExist(Integer deviceId) {
        return deviceMapper.existsWithPrimaryKey(deviceId);
    }

    @Override
    public boolean checkPollutionTypeIsExist(Integer pollutionTypeId) {
        return pollutionTypeMapper.existsWithPrimaryKey(pollutionTypeId);
    }

    @Override
    public boolean checkWaterSourceIsExist(Integer waterSourceId) {
        return waterSourceMapper.existsWithPrimaryKey(waterSourceId);
    }

    @Override
    public String getApplyStatusName(String status) {
        String statusName = applyStatusDict.get(status);
        if (ToolUtil.isEmpty(statusName)) {
            return "--";
        } else {
            return statusName;
        }
    }

    @Override
    public String getApplyTypeName(String applyType) {
        String typeName = applyTypeDict.get(applyType);
        if (ToolUtil.isEmpty(typeName)) {
            return "--";
        } else {
            return typeName;
        }
    }

    @Override
    public String getDeviceName(Integer deviceId) {
        Device device = deviceMapper.selectByPrimaryKey(deviceId);
        if (device == null) {
            return "--";
        }
        return device.getName();
    }

    @Override
    @Cacheable(key = "#value + #pollutionTypeId + 'overStandard'")
    public boolean isOverStandard(Double value, Integer pollutionTypeId) {
        PollutionType pollutionType = pollutionTypeMapper.selectByPrimaryKey(pollutionTypeId);
        if (pollutionType == null) {
            return false;
        }
        Double limitValue = pollutionType.getLimitvalue();
        return value > limitValue;
    }

    @Override
    @Cacheable(key = "#waterType + 'waterType'")
    public String getWaterTypeName(String waterType) {
        String waterTypeName = waterTypeDict.get(waterType);
        if (waterTypeName == null) {
            return "--";
        } else {
            return waterTypeName;
        }
    }

    /**
     * 根据水源id获取水源名称
     */
    @Override
    @Cacheable(key = "#waterSourceId + 'water'")
    public WaterSource getWaterSourceById(Integer waterSourceId) {
        WaterSource waterSource = waterSourceMapper.selectByPrimaryKey(waterSourceId);
        return waterSource;
    }

    /**
     * 根据用户id获取用户名称
     */
    @Override
    public String getUserNameById(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user != null) {
            return user.getName();
        } else {
            return "--";
        }
    }

    /**
     * 根据用户id获取用户账号
     */
    @Override
    public String getUserAccountById(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user != null) {
            return user.getAccount();
        } else {
            return "--";
        }
    }

    /**
     * 通过角色ids获取角色名称
     */
    @Override
    public String getRoleName(String roleIds) {
        Integer[] roles = Convert.toIntArray(roleIds);
        StringBuilder sb = new StringBuilder();
        for (int role : roles) {
            Role roleObj = roleMapper.selectByPrimaryKey(role);
            if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
                sb.append(roleObj.getName()).append(",");
            }
        }
        return StrKit.removeSuffix(sb.toString(), ",");
    }

    /**
     * 通过角色id获取角色名称
     */
    @Override
    @Cacheable(key = "#roleId + 'role'")
    public String getSingleRoleName(Integer roleId) {
        if (0 == roleId) {
            return "--";
        }
        Role roleObj = roleMapper.selectByPrimaryKey(roleId);
        if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getName();
        }
        return "";
    }

    /**
     * 通过角色id获取角色英文名称
     */
    @Override
    public String getSingleRoleTip(Integer roleId) {
        if (0 == roleId) {
            return "--";
        }
        Role roleObj = roleMapper.selectByPrimaryKey(roleId);
        if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getTips();
        }
        return "";
    }

    /**
     * 获取部门名称
     */
    @Override
    public String getDeptName(Integer deptId) {
        Dept dept = deptMapper.selectByPrimaryKey(deptId);
        if (ToolUtil.isNotEmpty(dept) && ToolUtil.isNotEmpty(dept.getFullname())) {
            return dept.getFullname();
        }
        return "";
    }

    /**
     * 获取菜单的名称们(多个)
     */
    @Override
    public String getMenuNames(String menuIds) {
        Integer[] menus = Convert.toIntArray(menuIds);
        StringBuilder sb = new StringBuilder();
        for (int menu : menus) {
            Menu menuObj = menuMapper.selectByPrimaryKey(menu);
            if (ToolUtil.isNotEmpty(menuObj) && ToolUtil.isNotEmpty(menuObj.getName())) {
                sb.append(menuObj.getName()).append(",");
            }
        }
        return StrKit.removeSuffix(sb.toString(), ",");
    }

    /**
     * 获取菜单名称
     */
    @Override
    public String getMenuName(Integer menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            return "";
        } else {
            Menu menu = menuMapper.selectByPrimaryKey(menuId);
            if (menu == null) {
                return "";
            } else {
                return menu.getName();
            }
        }
    }

    /**
     * 获取菜单名称通过编号
     */
    @Override
    public String getMenuNameByCode(String code) {
        if (ToolUtil.isEmpty(code)) {
            return "";
        } else {
            Menu param = new Menu();
            param.setCode(code);
            Menu menu = menuMapper.selectOne(param);
            if (menu == null) {
                return "";
            } else {
                return menu.getName();
            }
        }
    }

    /**
     * 获取字典名称
     */
    @Override
    public String getDictName(Integer dictId) {
        if (ToolUtil.isEmpty(dictId)) {
            return "";
        } else {
            Dict dict = dictMapper.selectByPrimaryKey(dictId);
            if (dict == null) {
                return "";
            } else {
                return dict.getName();
            }
        }
    }

    /**
     * 获取通知标题
     */
    @Override
    public String getNoticeTitle(Integer dictId) {
        if (ToolUtil.isEmpty(dictId)) {
            return "";
        } else {
            Notice notice = noticeMapper.selectByPrimaryKey(dictId);
            if (notice == null) {
                return "";
            } else {
                return notice.getTitle();
            }
        }
    }

    /**
     * 根据字典名称和字典中的值获取对应的名称
     */
    @Override
    public String getDictsByName(String name, Integer val) {
        Dict temp = new Dict();
        temp.setName(name);
        Dict dict = dictMapper.selectOne(temp);
        if (dict == null) {
            return "";
        } else {
            Example example = new Example(Dict.class);
            example.createCriteria().andEqualTo("pid", dict.getId());
            List<Dict> dicts = dictMapper.selectByExample(example);
            for (Dict item : dicts) {
                if (item.getNum() != null && item.getNum().equals(val)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    /**
     * 获取性别名称
     */
    @Override
    public String getSexName(Integer sex) {
        return getDictsByName("性别", sex);
    }

    /**
     * 获取用户登录状态
     */
    @Override
    public String getStatusName(Integer status) {
        return ManagerStatus.valueOf(status);
    }

    /**
     * 获取菜单状态
     */
    @Override
    public String getMenuStatusName(Integer status) {
        return MenuStatus.valueOf(status);
    }

    /**
     * 查询字典
     */
    @Override
    public List<Dict> findInDict(Integer id) {
        if (ToolUtil.isEmpty(id)) {
            return null;
        } else {
            Example example = new Example(Dict.class);
            example.createCriteria().andEqualTo("pid", id);
            List<Dict> dicts = dictMapper.selectByExample(example);
            if (dicts == null || dicts.size() == 0) {
                return null;
            } else {
                return dicts;
            }
        }
    }

    /**
     * 获取被缓存的对象(用户删除业务)
     */
    @Override
    public String getCacheObject(String para) {
        return LogObjectHolder.me().get().toString();
    }

    /**
     * 获取子部门id
     */
    @Override
    public List<Integer> getSubDeptId(Integer deptid) {
        Example example = new Example(Dept.class);
        example.createCriteria().andLike("pids", "%[" + deptid + "]%");

        List<Dept> depts = this.deptMapper.selectByExample(example);

        ArrayList<Integer> deptids = new ArrayList<>();

        if (depts != null || depts.size() > 0) {
            for (Dept dept : depts) {
                deptids.add(dept.getId());
            }
        }

        return deptids;
    }

    /**
     * 获取所有父部门id
     */
    @Override
    public List<Integer> getParentDeptIds(Integer deptid) {
        Dept dept = deptMapper.selectByPrimaryKey(deptid);
        String pids = dept.getPids();
        String[] split = pids.split(",");
        ArrayList<Integer> parentDeptIds = new ArrayList<>();
        for (String s : split) {
            parentDeptIds.add(Integer.valueOf(StrKit.removeSuffix(StrKit.removePrefix(s, "["), "]")));
        }
        return parentDeptIds;
    }

    @Override
    public String getDateTime(Long updateTime) {
        if (updateTime == null) {
            return "--";
        }
        return DateUtil.format(new Date(updateTime), "yyyy-MM-dd HH:mm:ss");
    }


}
