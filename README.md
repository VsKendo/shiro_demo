# At first
This is a shiro demo(NOT MVC), more more more than "Hello World" of course;\
Unfortunately, My english is su*k!

ONLY CHINESE HERE! including comment and readme file\
ONLY CHINESE HERE! including comment and readme file\
ONLY CHINESE HERE! including comment and readme file

if you not good at chinese, a google translator might be help\
else if you dont know chinese at all , ONLY SORRY.

# shiro_demo
springboot整合shiro的demo  
场景是这样的：模拟一个图书管理系统
有admin和user普通用户,没登录的是guest游客
后面有说权限的细则

本demo是前后端分离的,统一返回json,不会进行转跳
## 说明
本仓库仅仅是测试springboot整合shiro框架，dao层没有真正访问数据库，
而是模拟从数据库中取数据，所以不需要什么环境配置。
````
package com.ddu.shiro_demo.dao;

import ...

@Component
public class PermissionDao {

    static List<Permission> db;

    static {
        db = new ArrayList<>();
        db.add(new Permission("p001","add"));
        db.add(new Permission("p002","delete"));
        db.add(new Permission("p003","update"));
        db.add(new Permission("p004","select"));
    }
    ...
}
````
//开始吐槽，这个数据库的实现是另一个人写的。我想改改逻辑但是后面放弃了。直接写死在里面吧，开发时再说。

为了更好的模拟权限管理，我设置了三个用户，两个角色：
- 用户：user01，user02，user03
- 角色：admin，user  
- 权限：add，delete，update，select
- 对应关系 
````
|用户        |角色     |权限                         
|user01     |admin    |add,delete,update,select  
|user02     |user     |add,select   
|guest      |guest    |select  
````

还需要说明的是``controller``包下的接口，该包下有三个类：
- `BookController`   
给类下有四类接口，分别是增加，删除，修改，查询，权限控制主要也是针对这些接口，
我们需要让上面定义的三个用户**完成登录**后，依据**自己的权限**来进行访问。
- `LoginController`  
该类下有登录接口，所有人都能访问该接口进行登录，如果没有登录，
不允许访问``BookController``类下所有的接口
- `TestController`  
该类下有一个测试接口，用于测试项目是否真正，所有人都可访问  

我们这个项目端口使用的是`8081`，根目录为`/sd`，url样例为：
``http://localhost:8081/sd/test``，如果返回``10000``（如下所示），
不要紧张，这正常的，因为代码还有很多问题，需要你自行解决。
````
{
    "code": 10000,
    "success": false,
    "message": "账号或密码错误",
    "data": null
}
````
## 完善者说明    
这个代码其实挺臃肿的，应该要 AOP ，即面向切面编程。但是我还不太会，所以放弃了 
反正每次返回的那个CommonResult就是json啦    
我说说我加的东西    
- `BookController`  
增加了权限限制，主要在config.java的map里面    
因为我觉得配置应该写进数据库里，包括权限限制。而config.java里的map可以实现访问数据库来更新，所以我这样干。    
- `LoginController`  
加了很多东西，包括subject的登录验证，logout的逻辑
- `TestController`  
just let it go .我没做任何修改     
其他的，我在config里加了很多，包括自定义了rememberMeManager等，我觉得对实际开发有一定参考价值  
建议你从controller看起，然后慢慢摸清整个代码的逻辑. 

update time: 2019-11-05(YYYY : M M : DD)