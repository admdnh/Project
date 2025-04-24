//控制层使用调用接口
package com.wc.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wc.springboot.beans.Final_User;
import com.wc.springboot.beans.UserDto;
import com.wc.springboot.common.Constants;
import com.wc.springboot.common.Result;
import com.wc.springboot.mapper.UserMapper;
import com.wc.springboot.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDto user){
        String username = user.getUsername();
        String password = user.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDto dto = userService.login(user);
        return Result.success(dto);
    }
    @PostMapping("/register")
    public Result register(@RequestBody UserDto user){
        String username = user.getUsername();
        String password = user.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        Final_User dto = userService.register(user);
        return Result.success(dto);
    }
    //新增和修改数据
    @PostMapping
    public boolean save(@RequestBody Final_User user){
        return userService.saveUser(user);
    }
    //查询所有数据
    @GetMapping
    public List<Final_User> index(){
        return userService.list();
    }
    //查询个人信息
    @GetMapping("/username/{username}")
    public Final_User findOne(@PathVariable String username){
        QueryWrapper<Final_User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return userService.getOne(queryWrapper);
    }
    //删除数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return userService.removeById(id);
    }
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return userService.removeByIds(ids);
    }
//    @GetMapping("/page")
//    public  Map<String,Object> findPage(@RequestParam Integer pageNum,
//                                        @RequestParam Integer pageSize,
//                                        @RequestParam String username){
//        pageNum = (pageNum-1)*pageSize;
//        List<Final_User> data = userMapper.selectPage(pageNum,pageSize,username);
//        Integer total =  userMapper.selectTotal(username);
//        Map<String,Object> res = new HashMap<>();
//        res.put("data",data);
//        res.put("total",total);
//        return res;
//    }
    //mybatis-plus方式分页查询
    @GetMapping("/page")
    public  IPage<Final_User> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String username,
                                        @RequestParam(defaultValue = "") String email,
                                        @RequestParam(defaultValue = "") String address){
        IPage<Final_User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Final_User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(Strings.isNotEmpty(username),"username",username);
        queryWrapper.like(Strings.isNotEmpty(email),"email",email);
        queryWrapper.like(Strings.isNotEmpty(address),"address",address);
        queryWrapper.orderByDesc("id");
        return userService.page(page,queryWrapper);
    }


     //导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Final_User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
        // ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    //excel 导入
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);
        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Final_User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Final_User user = new Final_User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            user.setAvatarUrl(row.get(6).toString());
            users.add(user);
        }
        return userService.saveBatch(users);
    }
}
