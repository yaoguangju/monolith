package com.mochen.sharding.entity.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String token;
    private UserVO userVO;

    @Data
    public static class UserVO{
        private Long id;
        private String username;
        private String name;
        private Long schoolId;
        private Long year;
    }

}
