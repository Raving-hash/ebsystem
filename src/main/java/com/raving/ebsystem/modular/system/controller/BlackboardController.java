package com.raving.ebsystem.modular.system.controller;

import com.raving.ebsystem.common.controller.BaseController;
import com.raving.ebsystem.common.persistence.dao.NoticeMapper;
import com.raving.ebsystem.core.shiro.ShiroKit;
import com.raving.ebsystem.core.support.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 总览信息
 */
@Controller
@RequestMapping("/blackboard")
public class BlackboardController extends BaseController {

    @Autowired
    NoticeMapper noticeMapper;

    /**
     * 跳转到黑板
     */
    @RequestMapping("")
    public String blackboard(Model model) {
        Integer type = 0;
        // 获取最近7天的
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        DateTime dateTime = new DateTime(calendar.getTime());
        List<Map<String, Object>> notices = noticeMapper.selectAllBefore(dateTime);
        if (ShiroKit.isAdmin() || Objects.requireNonNull(ShiroKit.getUser()).getDeptId() != null && ShiroKit.getUser().getDeptId().equals(28)) {
            type = 10;
            List<Map<String, Object>> notices2 = noticeMapper.selectAllByType(type);
            // 合并两个notices 和 notices2
            notices.addAll(notices2);
        }

        model.addAttribute("noticeList",notices);
        return "/blackboard.html";
    }
}
