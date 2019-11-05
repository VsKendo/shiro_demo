package com.ddu.shiro_demo.config;

import com.ddu.shiro_demo.filter.MyFormAuthenticationFilter;
import com.ddu.shiro_demo.filter.MyLogoutFilter;
import com.ddu.shiro_demo.filter.MyRolesAuthorizationFilter;
import com.ddu.shiro_demo.realm.CustomRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Configuration
public class ShiroConfig {
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        creator.setUsePrefix(true);
        return creator;
    }

/**
 　　* 开启shiro aop注解支持.
 　　* 使用代理方式;所以需要开启代码支持;
 　　*/
       @Bean
public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
 AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
 authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
return authorizationAttributeSourceAdvisor;
}

    //将自己的验证方式加入容器
    @Bean
    public CustomRealm myShiroRealm() {
        CustomRealm customRealm = new CustomRealm();
        //替换当前 Realm 的 credentialsMatcher 属性. 使用 HashedCredentialsMatcher 对象, 并设置加密算法即可.
        HashedCredentialsMatcher hashedCredentialsMatcher = new  HashedCredentialsMatcher("MD5");
        //循环加密两次
        hashedCredentialsMatcher.setHashIterations(2);
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return customRealm;
    }

    /**
     * 权限管理，配置主要是Realm的管理认证,采用多Realm验证
     * @return securityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        //有很多策略，这里采取 FirstSuccessfulStrategy
        modularRealmAuthenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        securityManager.setAuthenticator(modularRealmAuthenticator);
        List<Realm> realms = new ArrayList<>();
        //可添加新realm
        realms.add(myShiroRealm());
        //原本是setRealm，现在是setRealms
        securityManager.setRealms(realms);
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    public SimpleCookie rememberMeCookie(){
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //cookie生效时间30天,单位秒;一时3600;一天86400;一月2592000;不要用一年
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // cookieRememberMeManager.setCipherKey用来设置加密的Key,参数类型byte[],字节数组长度要求16
        // 往深几层看源码,采用16位的数组最后会返回128位的值,而shiro默认使用128位。所以除非你改了，否则传入要求长度16
        //原串
        String keyStr="吕翔写bug";
        byte[] keys=keyStr.getBytes(StandardCharsets.UTF_8);
        //加密.因为秘钥是硬编码,泄露就有可能导致反序列化漏洞.一定要自己生成或者使用官方秘钥方法生成秘钥
        String encode = Base64Utils.encodeToString(Arrays.copyOf(keys, 16));
        //使用官方秘钥方法生成秘钥也可以。千万不要去网上找！否则反序列化漏洞会导致黑客执行任意代码
        //AbstractSymmetricCipherService abstractSymmetricCipherService = new AbstractSymmetricCipherService();
        //key encode2 = AbstractSymmetricCipherService.generateNewKey(16);
        //将加密后的字符串给Manager. cookie加密的密钥
        cookieRememberMeManager.setCipherKey(encode.getBytes());
        return cookieRememberMeManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //不能使用HashMap,否则在map中put过多的话会出错.使用这个是因为原代码里用的就是 LinkedHashMap
        LinkedHashMap<String, javax.servlet.Filter> filterMap = new LinkedHashMap<>();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        filterMap.put("authc", new MyFormAuthenticationFilter());
        filterMap.put("logout",new MyLogoutFilter());
        filterMap.put("roles",new MyRolesAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        //添加权限控制,此处手动输入
        //在实际开发中,通过数据库查询获得这个map,从而实现将资源权限存入数据库的操作
        //写死在配置文件里要重新启动,而写在数据库里可以动态更新页面的权限,我觉得用map也比注解好得多
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.clear();
        //对所有用户认证
        map.put("/login", "anon");
        map.put("/error", "anon");
        map.put("/index","anon");
        //map.put("/book","rest[admin:*,user:create,user:read,*:GET]");
        //map.put("/book**","rest[admin:delete,admin:put]");
        map.put("/logout", "logout");
        map.put("/hello", "roles[user]");
        map.put("/admin","roles[admin]");
        map.put("/**", "authc");
        //以下的一堆setURL没必要,因为前后端分离,应该返回json而不是返回转跳.
        //登录
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        //首页
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//        //错误页面，认证不通过跳转
//        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        //以上的只有mvc才这么写,应该实现权限不足返回json,目前还不知道怎么改
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

}
