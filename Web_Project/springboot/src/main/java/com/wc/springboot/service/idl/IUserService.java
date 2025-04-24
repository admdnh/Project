package com.wc.springboot.service.idl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wc.springboot.beans.Final_User;
import com.wc.springboot.beans.UserDto;

public interface IUserService extends IService<Final_User> {
    UserDto login(UserDto userDTO);
    Final_User register(UserDto userDTO);
}
