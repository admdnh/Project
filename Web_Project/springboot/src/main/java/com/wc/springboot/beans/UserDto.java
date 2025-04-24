package com.wc.springboot.beans;

import lombok.Data;

@Data
public class UserDto {//接收前端登录数据
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
}
