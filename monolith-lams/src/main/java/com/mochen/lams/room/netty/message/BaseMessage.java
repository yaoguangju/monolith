package com.mochen.lams.room.netty.message;

import lombok.Data;

@Data
public class BaseMessage {

    private Long sendUid;
    private Long acceptUid;
    private boolean isSendAll;
    private boolean isSendAllStudent;
    private boolean isSendTeacher;
    private String message;
}
