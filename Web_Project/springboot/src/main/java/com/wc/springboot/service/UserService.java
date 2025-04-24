package com.wc.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wc.springboot.beans.Final_User;
import com.wc.springboot.beans.UserDto;
import com.wc.springboot.common.Constants;
import com.wc.springboot.exception.ServiceException;
import com.wc.springboot.mapper.UserMapper;
import com.wc.springboot.service.idl.IUserService;
import com.wc.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service//注入到springboot中
public class UserService extends ServiceImpl<UserMapper,Final_User> implements IUserService {

    public boolean saveUser(Final_User user){
//        if(user.getId() == null){
//            return save(user);
//        }else{
//            return updateById(user);
//        }
        return saveOrUpdate(user);
    }
    private static final Log LOG = Log.get();
    @Override
    public UserDto login(UserDto userDto) {
        Final_User one = getUserInfo(userDto);
        if (one != null) {
            BeanUtil.copyProperties(one, userDto, true);
            // 设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDto.setToken(token);
            return userDto;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }
    @Override
    public Final_User register(UserDto userDto) {
        Final_User one = getUserInfo(userDto);
        if (one == null) {
            one = new Final_User();
            BeanUtil.copyProperties(userDto, one, true);
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    private Final_User getUserInfo(UserDto userDto) {
        QueryWrapper<Final_User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDto.getUsername());
        queryWrapper.eq("password", userDto.getPassword());
        Final_User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }
//    @Autowired
//    private UserMapper userMapper;

//    public int save(Final_User user){
//        if(user.getId() == null){
//            return userMapper.insert(user);
//        }else{
//            return userMapper.update(user);
//        }
//    }
}
