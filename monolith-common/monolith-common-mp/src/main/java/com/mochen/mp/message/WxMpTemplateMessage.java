package com.mochen.mp.message;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;


@Data
@Accessors(chain = true)
public class WxMpTemplateMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String touser;

    private String template_id;

    private String url;

    private MiniProgramData miniprogram;

    private Map<String,Object> data;

    public WxMpTemplateMessage(String touser, String template_id, String url, Map<String, Object> data) {
        this.touser = touser;
        this.template_id = template_id;
        this.url = url;
        this.data = data;
    }

    public WxMpTemplateMessage(String touser, String template_id, MiniProgramData miniprogram, Map<String, Object> data) {
        this.touser = touser;
        this.template_id = template_id;
        this.miniprogram = miniprogram;
        this.data = data;
    }
}
