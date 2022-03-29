package com.mochen.mp.manager.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mochen.core.exception.CommonException;
import com.mochen.mp.common.contanst.MpConstant;
import com.mochen.mp.manager.TemplateMessageManager;
import com.mochen.mp.message.WxMpTemplateMessage;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Component
public class TemplateMessageManagerImpl implements TemplateMessageManager {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public boolean sendTemplateMessage(WxMpTemplateMessage wxMpTemplateMessage) throws CommonException {

        ResponseEntity<String> accessTokenResponse = restTemplate.exchange(
                MpConstant.ACCESS_TOKEN_URL,
                HttpMethod.GET,
                null,
                String.class);

        String accessTokenResult = accessTokenResponse.getBody();
        JSONObject accessTokenJsonObject = JSONObject.parseObject(accessTokenResult);
        if(!accessTokenJsonObject.get("code").equals(20000)){
            throw new CommonException("普通accessToken获取失败");
        }
        String accessToken = accessTokenJsonObject.get("data").toString();
        // 设置 HTTP Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 设置请求 Entity
        HttpEntity<Object> entity = new HttpEntity<>(wxMpTemplateMessage, headers);
        // 发送请求
        String json = JSON.toJSONString(wxMpTemplateMessage);
        ResponseEntity<String> messageResponse = restTemplate.exchange(
                MpConstant.TEMPLATE_MESSAGE_URL + "?access_token=" + accessToken,
                HttpMethod.POST,
                entity,
                String.class);
        String messageResult = messageResponse.getBody();
        JSONObject jsonObject = JSONObject.parseObject(messageResult);
        return jsonObject.get("errcode").equals(0);
    }
}
