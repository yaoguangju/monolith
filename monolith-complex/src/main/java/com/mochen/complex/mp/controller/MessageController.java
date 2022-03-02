package com.mochen.complex.mp.controller;

import com.mochen.core.common.xbo.Result;
import com.mochen.core.exception.CommonException;
import com.mochen.mp.manager.TemplateMessageManager;
import com.mochen.mp.message.MiniProgramData;
import com.mochen.mp.message.WxMpTemplateData;
import com.mochen.mp.message.WxMpTemplateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mp")
@Slf4j
public class MessageController {

    @Resource
    private TemplateMessageManager templateMessageManager;

    /**
     * 发送模板消息示例(小程序)
     */
    @GetMapping("/send1")
    public Result send1() throws CommonException {
        MiniProgramData miniProgramData = new MiniProgramData();
        miniProgramData.setAppid("wxe5af72207b5e9ea0");
        miniProgramData.setPagepath(null);
        Map<String,Object> map = new HashMap<>();
        map.put("first",new WxMpTemplateData("恭喜您，成功报名线下体验课"));
        map.put("keyword1",new WxMpTemplateData("体验课名称"));
        map.put("keyword2",new WxMpTemplateData("体验课"));
        map.put("remark",new WxMpTemplateData("恭喜您，成功报名线下体验课时间"));

        WxMpTemplateMessage message = new WxMpTemplateMessage("oT0VQ6FygOFVH9wknVNDBj_woQSg", "AjjpDEwiggAXr6CGLRt-_7tGmttgnH5n7i-J6OB0sYQ",
                 miniProgramData, map);
        final boolean b = templateMessageManager.sendTemplateMessage(message);
        return Result.success(b);
    }

    /**
     * 发送模板消息示例(地址)
     */
    @GetMapping("/send2")
    public Result send2() throws CommonException {
        Map<String,Object> map = new HashMap<>();
        map.put("first",new WxMpTemplateData("恭喜您，成功报名线下体验课"));
        map.put("keyword1",new WxMpTemplateData("体验课名称"));
        map.put("keyword2",new WxMpTemplateData("体验课"));
        map.put("remark",new WxMpTemplateData("恭喜您，成功报名线下体验课时间"));

        WxMpTemplateMessage message = new WxMpTemplateMessage("oT0VQ6FygOFVH9wknVNDBj_woQSg", "AjjpDEwiggAXr6CGLRt-_7tGmttgnH5n7i-J6OB0sYQ",
                "https://www.baidu.com", map);
        final boolean b = templateMessageManager.sendTemplateMessage(message);
        return Result.success(b);
    }

}
