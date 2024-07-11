package com.raving.ebsystem.modular.pollution.controller;

import com.raving.ebsystem.common.annotion.Permission;
import com.raving.ebsystem.common.controller.BaseController;
import com.raving.ebsystem.common.persistence.dao.WaterSourceMapper;
import com.raving.ebsystem.common.persistence.model.WaterSource;
import com.raving.ebsystem.core.log.LogObjectHolder;
import com.raving.ebsystem.core.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/waterSource")
public class WaterSourceController extends BaseController {
    @Autowired
    private WaterSourceMapper waterSourceMapper;

    private String prefix = "/system/water_source/";

    @RequestMapping("")
    public String index() {
        return prefix + "water_source.html";
    }

    @RequestMapping("/getAdd")
    public String getAdd() {
        return prefix + "water_source_add.html";
    }

    @RequestMapping("/getEdit/{waterSourceId}")
    public String getEdit(@PathVariable(name = "waterSourceId") Integer waterSourceId, Model model) {
        if (ToolUtil.isEmpty(waterSourceId)) {
            throw new IllegalArgumentException("水源ID不能为空");
        }
        WaterSource waterSource = waterSourceMapper.selectByPrimaryKey(waterSourceId);
        if (waterSource == null) {
            throw new IllegalArgumentException("该水源不存在");
        }
        model.addAttribute(waterSource);
        LogObjectHolder.me().set(waterSource);
        return prefix + "water_source_edit.html";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list(String condition) {
        if (ToolUtil.isEmpty(condition)) {
            condition = "";
        }
        return waterSourceMapper.list(condition);
    }

    @RequestMapping("/add")
    @ResponseBody
    @Permission
    public Object addWaterSource(WaterSource waterSource) {
        waterSource.setId(null);
        waterSourceMapper.insert(waterSource);
        return SUCCESS_TIP;
    }

    @RequestMapping("/edit")
    @ResponseBody
    @Permission
    public Object editWaterSource(WaterSource waterSource) {
        waterSourceMapper.updateByPrimaryKey(waterSource);
        return SUCCESS_TIP;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @Permission
    public Object deleteWaterSource(Integer id) {
        waterSourceMapper.deleteByPrimaryKey(id);
        return SUCCESS_TIP;
    }
}
