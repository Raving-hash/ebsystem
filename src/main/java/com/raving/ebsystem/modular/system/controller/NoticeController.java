package com.raving.ebsystem.modular.system.controller;

import com.raving.ebsystem.common.annotion.log.BussinessLog;
import com.raving.ebsystem.common.constant.Dict;
import com.raving.ebsystem.common.constant.factory.ConstantFactory;
import com.raving.ebsystem.common.controller.BaseController;
import com.raving.ebsystem.common.exception.BizExceptionEnum;
import com.raving.ebsystem.common.exception.BussinessException;
import com.raving.ebsystem.common.persistence.dao.NoticeMapper;
import com.raving.ebsystem.common.persistence.dao.WarnWaterMapper;
import com.raving.ebsystem.common.persistence.model.Notice;
import com.raving.ebsystem.core.log.LogObjectHolder;
import com.raving.ebsystem.core.shiro.ShiroKit;
import com.raving.ebsystem.core.util.ToolUtil;
import com.raving.ebsystem.modular.pollution.wrapper.WarnWaterWrapper;
import com.raving.ebsystem.modular.system.warpper.NoticeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 通知控制器
 */
@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {

    private String PREFIX = "/system/notice/";

    @Resource
    private NoticeMapper noticeMapper;

    @Autowired
    private WarnWaterMapper warnWaterMapper;

    /**
     * 跳转到通知列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "notice.html";
    }

    /**
     * 跳转到添加通知
     */
    @RequestMapping("/notice_add")
    public String noticeAdd() {
        return PREFIX + "notice_add.html";
    }

    /**
     * 跳转到修改通知
     */
    @RequestMapping("/notice_update/{noticeId}")
    public String noticeUpdate(@PathVariable Integer noticeId, Model model) {
        Notice notice = noticeMapper.selectByPrimaryKey(noticeId);
        model.addAttribute("notice",notice);
        LogObjectHolder.me().set(notice);
        return PREFIX + "notice_edit.html";
    }

    /**
     * 跳转到首页通知
     */
    @RequestMapping("/hello")
    public String hello() {
        List<Map<String, Object>> notices = noticeMapper.list(null);
        super.setAttr("noticeList",notices);
        return "/blackboard.html";
    }

    /**
     * 获取通知列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = noticeMapper.list(condition);
        return super.warpObject(new NoticeWrapper(list));
    }

    /**
     * 新增通知
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增通知",key = "title",dict = Dict.NoticeMap)
    public Object add(Notice notice) {
        if (ToolUtil.isOneEmpty(notice, notice.getTitle(), notice.getContent())) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        notice.setCreater(ShiroKit.getUser().getId());
        notice.setCreatetime(new Date());
        notice.setType(0);
        noticeMapper.insert(notice);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除通知
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @BussinessLog(value = "删除通知",key = "noticeId",dict = Dict.DeleteDict)
    public Object delete(@RequestParam Integer noticeId) {
        //缓存通知名称
        LogObjectHolder.me().set(ConstantFactory.me().getNoticeTitle(noticeId));
        noticeMapper.deleteByPrimaryKey(noticeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改通知
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改通知",key = "title",dict = Dict.NoticeMap)
    public Object update(Notice notice) {
        if (ToolUtil.isOneEmpty(notice, notice.getId(), notice.getTitle(), notice.getContent())) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        Notice old = noticeMapper.selectByPrimaryKey(notice.getId());
        old.setTitle(notice.getTitle());
        old.setContent(notice.getContent());
        noticeMapper.updateByPrimaryKey(old);
        return super.SUCCESS_TIP;
    }

    /**
     * 获取最近的报警次数
     *
     */
    @RequestMapping(("/getWarnCount"))
    @ResponseBody
    public Object getWarnCount() {
        Map<String, Integer> day2count = new HashMap<>();
        day2count.put("day1", (Integer) getWarnCountByWaterSourceIdBefore(null, 1));
        day2count.put("day7", (Integer) getWarnCountByWaterSourceIdBefore(null, 7));
        day2count.put("day30", (Integer) getWarnCountByWaterSourceIdBefore(null, 30));
        //转换成json
        return super.warpObject(new WarnWaterWrapper(day2count));

    }

    /**
     * 获取最近day天的警报水源列表，降序
     * @return Object
     */
    @RequestMapping(("/getWarnWaterSourceRank"))
    @ResponseBody
    public Object getWarnWaterSourceRank(@RequestParam(name = "day") Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        LocalDateTime dateTime = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        List<Map<String, Object>> list = warnWaterMapper.selectWarnWaterSourceRank(formattedDateTime);
        return super.warpObject(new WarnWaterWrapper(list));
    }

    public Object getWarnCountByWaterSourceIdBefore(Integer waterSourceId, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        LocalDateTime dateTime = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        return warnWaterMapper.selectCountByWaterSourceIdBefore(waterSourceId, formattedDateTime);
    }

}
