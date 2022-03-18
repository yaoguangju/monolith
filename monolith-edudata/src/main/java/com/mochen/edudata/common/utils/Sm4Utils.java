package com.mochen.edudata.common.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.mochen.edudata.common.config.CustomizeConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Sm4Utils {

    @Resource
    private CustomizeConfig customizeConfig;

    public String getDecryptStr(String stringHex){
        String key = customizeConfig.getSm4key();
        SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes());
        return sm4.decryptStr(stringHex, CharsetUtil.CHARSET_UTF_8);
    }

}
