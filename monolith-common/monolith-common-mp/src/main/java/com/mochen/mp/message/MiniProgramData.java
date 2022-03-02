package com.mochen.mp.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class MiniProgramData  implements Serializable {

    private static final long serialVersionUID = 1L;
    private String appid;
    private String pagepath;
}
