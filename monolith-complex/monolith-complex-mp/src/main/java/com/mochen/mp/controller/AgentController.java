package com.mochen.mp.controller;


import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.mochen.mp.common.contanst.CommonConstant;
import com.mochen.redis.common.manager.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
//@Controller
@RequestMapping("/mp")
public class AgentController {

    /**
     * 用户统一授权的一个示例
     * 传入需要授权的页面，最终重定向到原页面，附带token或者用户信息等参数
     */

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RedisManager redisManager;

    @RequestMapping("agent")
    public ModelAndView agent(HttpServletRequest request) {
        String code = request.getParameter("code");
        if(StringUtils.isEmpty(code)){
            // 先将用户自定义的redirectUrl存入缓存
            String redirectUrl = request.getParameter("redirectUrl");
            log.info("缓存redirectUrl,{}",redirectUrl);
            String redirectKey = RandomUtil.randomString(10);
            redisManager.setCacheObject(redirectKey,redirectUrl,30L);
            // 获取到code
            String agentUrl = CommonConstant.AGENT_URL + "?redirectKey=" + redirectKey;
            // 随机生成state
            String state = RandomUtil.randomString(6);
            String buildUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ CommonConstant.MAJOR_APP_ID + "&redirect_uri="+ agentUrl +"&response_type=code&scope=snsapi_userinfo&state=" + state + "#wechat_redirect";
            return new ModelAndView("redirect:" + buildUrl);
        }
        else{
            String redirectKey = request.getParameter("redirectKey");
            String wxUrl = "redirect:" + CommonConstant.CALLBACK_URL + "?code=" + code  + "&redirectKey=" + redirectKey;
            return new ModelAndView(wxUrl);
        }
    }

    @RequestMapping("/callback")
    public ModelAndView callback(HttpServletRequest request) {
        String code = request.getParameter("code");
        String redirectKey = request.getParameter("redirectKey");
        // 从缓存中查出用户自定义的redirectUrl
        String redirectUrl = redisManager.getCacheObject(redirectKey);
        log.info("接收redirectUrl,{}",redirectUrl);
        String getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + CommonConstant.MAJOR_APP_ID +"&secret="+ CommonConstant.MAJOR_APP_SECRET+"&code="+ code +"&grant_type=authorization_code";
        ResponseEntity<String> accessTokenResponse = restTemplate.exchange(
                getAccessTokenUrl,
                HttpMethod.GET,
                null,
                String.class);
        String accessTokenResult = accessTokenResponse.getBody();
        JSONObject jsonObject = JSONObject.parseObject(accessTokenResult);
        log.info("access_token,{}",jsonObject.get("access_token"));
        log.info("openid,{}",jsonObject.get("openid"));
        log.info("unionid,{}",jsonObject.get("unionid"));
        // 生成token
        String token = "1231312321312";
        String wxUrl = "redirect:" + redirectUrl + "?token=" + token + "&openid=" + jsonObject.get("openid");
        return new ModelAndView(wxUrl);
    }
}
