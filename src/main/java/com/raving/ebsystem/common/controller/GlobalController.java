package com.raving.ebsystem.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 全局的控制器
 */
@Controller
@RequestMapping("/global")
public class GlobalController {

    /**
     * 跳转到404页面
     */
    @RequestMapping(path = "/error")
    public String errorPage() {
        return "/login.html";
    }

    /**
     * 跳转到session超时页面
     */
    @RequestMapping(path = "/sessionError")
    public String errorPageInfo(Model model) {
        model.addAttribute("tips", "session超时");
        return "/login.html";
    }
}
