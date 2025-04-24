create database final;
use final;

create table final_user(
	id int auto_increment primary key comment '编号',
    username varchar(50) comment '用户名',
    password varchar(50) comment '密码',
    nickname varchar(50) comment '昵称',
    email varchar(50) comment '邮箱',
    phone varchar(20) comment '电话',
    address varchar(255) comment '地址',
    create_time timestamp default current_timestamp comment'创建时间'
)comment '用户信息表';

insert into final_user(username,password,nickname,email,phone,address) values
("admin","admin","管理员","admin@qq.com","123456789","江西南昌");

insert into final_user(username,password,nickname,email,phone,address) values
("wang","123","黑客帝国","wang@163.com","123895549","江西上饶"),
("sun","456","孙悟空","sun@qq.com","12345","花果山"),
("zhu","666","猪八戒","zhu@qq.com","6789","高老庄");

insert into final_user(username,password,nickname,email,phone,address) values
("wasss","9999","隐秘的角落","45656@163.com","1238895549","未知"),
("sssdf","556464","三七二十一","988@qq.com","12345565","小卖部"),
("ppp","777","无敌暴龙战神","666@qq.com","6788989","逸夫小学");

ALTER TABLE final_user ADD COLUMN avater_url varchar(255) default null comment '头像';

create table final_file(
	id int auto_increment primary key comment '编号',
    name varchar(255) comment '文件名称',
    type varchar(255) comment '文件类型',
    size bigint(20) comment '文件大小',
    url varchar(255) comment '下载链接',
    is_delete tinyint(1) default '0' comment '是否删除',
    enable tinyint(1) default '1' comment '是否禁用链接',
    md5 varchar(255) comment '文件MD5'
)comment '文件信息表';