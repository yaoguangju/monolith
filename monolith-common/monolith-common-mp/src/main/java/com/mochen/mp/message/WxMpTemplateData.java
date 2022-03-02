package com.mochen.mp.message;

import lombok.Data;

import java.io.Serializable;


@Data
public class WxMpTemplateData  implements Serializable {

    private static final long serialVersionUID = 1L;
    private String value;
    private String color;


    public WxMpTemplateData(String value) {
        this.value = value;
        this.color = "#173177";
    }

    public WxMpTemplateData(String value,String color) {
        this.value = value;
        this.color = color;
    }



}
