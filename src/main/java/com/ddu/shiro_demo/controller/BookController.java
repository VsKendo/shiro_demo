package com.ddu.shiro_demo.controller;

import com.ddu.shiro_demo.realm.CustomRealm;
import com.ddu.shiro_demo.utils.CommonResult;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    CustomRealm realm;
    @Resource
    LifecycleBeanPostProcessor a;
    @Resource
    DefaultAdvisorAutoProxyCreator b;
    @Resource
    AuthorizationAttributeSourceAdvisor c;
    /**
     * 查询所有书本记录
     *
     * @param currentPage 当前页数
     * @param pageSize    每页显示的总记录数
     * @return
     */
    @RequiresGuest
    @GetMapping("/list")
    public CommonResult list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        return CommonResult.success("查询所有书本记录");
    }

    /**
     * 查询单条记录
     *
     * @param id
     * @return
     */
    @RequiresGuest
    @GetMapping
    public CommonResult getOne(@RequestParam("id") String id) {
        return CommonResult.success("查询单条记录");
    }

    /**
     * 添加书本记录
     *
     * @param name
     * @return
     */
    @RequiresRoles(value = {"user", "admin"},logical= Logical.OR)
    @PostMapping
    public CommonResult register(@RequestParam("name") String name) {
        return CommonResult.success("添加书本记录");
    }

    /**
     * 更新
     *
     * @param id
     * @param name
     * @return
     */
    @RequiresRoles("admin")
    @PutMapping
    public CommonResult amend(@RequestParam("id") String id, @RequestParam("name") String name) {
        return CommonResult.success("更新");
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequiresRoles(value = {"admin"},logical = Logical.AND)
    @DeleteMapping
    public CommonResult delete(@RequestParam("id") String id) {
        System.out.println(realm);
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        return CommonResult.success("删除");
    }
}
