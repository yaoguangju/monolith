package com.mochen.lams.room.netty.message;

import lombok.Data;

@Data
public class Message {

    private String classId;
    private String type;
    private String content;
}
