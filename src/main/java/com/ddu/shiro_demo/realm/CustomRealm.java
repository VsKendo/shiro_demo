package com.ddu.shiro_demo.realm;

import com.ddu.shiro_demo.bean.User;
import com.ddu.shiro_demo.service.LoginService;
import com.ddu.shiro_demo.service.PermissionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

//继承AuthorizingRealm以后,每次login或者查询权限时都会使用这个类的方法
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;
    private PermissionService permissionService;


    /**
     * 提供用户信息返回权限信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject = SecurityUtils.getSubject();
        Set<String> roles = new HashSet<>();
        if(!subject.isAuthenticated()){
            roles.add("guest");
        }
        else{
            String id = (String)subject.getSession().getAttribute("id");
            //这里根据id查询对应权限,吕翔数据库表没写好,此行应该是数据库查询然后赋值给name
            String name;
            if(id.equals("u001")){
                name="admin";
            }
            else{
                name="user";
            }
            roles.add(name);
        }

        return new SimpleAuthorizationInfo(roles);
    }

    /**
     * 提供账户信息返回认证信息
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws ShiroException {
        //1. 把 AuthenticationToken 转换为 UsernamePasswordToken
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        //2. 获取 username
        String username = usernamePasswordToken.getUsername();
        //3. 调用数据库的方法
        User user=loginService.Login(username);
        //4. 根据情况, 决定抛出异常.
        if(user == null){
            throw new UnknownAccountException();
        }
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setAttribute("id",user.getId());
        //5. 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为: SimpleAuthenticationInfo
        //1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象.
        //2). credentials: 密码.
        //3). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
        //4). 采用用户名来生成盐(也可以采用 用户名+特定盐 来生成盐)使用盐值后,即使多个用户原始密码相同，它们存在数据库内结果也不同
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        //下面这个类会验证token中用户输入和数据库存的密码是否相同
        return new SimpleAuthenticationInfo(username,user.getPassword(),credentialsSalt,this.getName());
        //return new SimpleAuthenticationInfo(username,password,this.getName());
    }

}
