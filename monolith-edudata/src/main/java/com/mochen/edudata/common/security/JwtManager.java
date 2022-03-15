package com.mochen.edudata.common.security;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.mochen.core.exception.CommonException;
import com.mochen.edudata.common.contanst.CommonConstant;
import com.mochen.edudata.common.config.CustomizeConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class JwtManager {

    @Resource
    private CustomizeConfig customizeConfig;

    public String createToken(Long userId,Long year) {
        String jwtSecret = customizeConfig.getJwtSecret();
        JWTSigner signer = JWTSignerUtil.hs256(jwtSecret.getBytes());

        return JWT.create()
                .setPayload("uid", userId)
                .setPayload("expire_time", System.currentTimeMillis() + CommonConstant.TOKEN_EXPIRATION)
                .sign(signer);
    }

    public Long getUserIdByToken(String token) throws CommonException {
        this.checkJWT(token);
        JWT jwt = JWTUtil.parseToken(token);
        return Long.valueOf(jwt.getPayload("uid").toString());
    }


    private void checkJWT(String token) throws CommonException {
        String jwtSecret = customizeConfig.getJwtSecret();
        JWTSigner signer = JWTSignerUtil.hs256(jwtSecret.getBytes());
        if(!JWTUtil.verify(token, signer)){
            throw new CommonException("token错误");
        }
        JWT jwt = JWTUtil.parseToken(token);
        Long expireTime = (Long) jwt.getPayload("expire_time");
        if(expireTime <= System.currentTimeMillis()){
            throw new CommonException("token已经过期");
        }
    }
}
