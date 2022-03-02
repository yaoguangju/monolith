package com.mochen.mp.manager;


import com.mochen.core.exception.CommonException;
import com.mochen.mp.message.WxMpTemplateMessage;

public interface TemplateMessageManager {

    boolean sendTemplateMessage(WxMpTemplateMessage wxMpTemplateMessage) throws CommonException;

}
