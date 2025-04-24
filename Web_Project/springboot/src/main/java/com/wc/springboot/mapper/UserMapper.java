//声明一个调用数据库的接口
package com.wc.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wc.springboot.beans.Final_User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper extends BaseMapper<Final_User> {
//
//    @Select("select * from final_user")
//    List<Final_User> findAll();
//
//    @Insert("insert into final_user(username,password,nickname,email,phone,address) values" +
//            "(#{username},#{password},#{nickname},#{email},#{phone},#{address})")
//    int insert(Final_User user);
//
//    int update(Final_User user);
//
//    @Delete("delete from final_user where id = #{id}")
//    int deleteById(@Param("id") Integer id);
//
//    @Select("select * from final_user where username like concat('%',#{username},'%') limit #{pageNum},#{pageSize}")
//    List<Final_User> selectPage(Integer pageNum, Integer pageSize,String username);
//
//    @Select("select count(*) from final_user where username like concat('%',#{username},'%')")
//    Integer selectTotal(String username);
}
