package com.ddu.shiro_demo.filter;

import com.ddu.shiro_demo.utils.CommonResult;
import com.ddu.shiro_demo.utils.makeJson;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.json.JSONObject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;

public class MyRolesAuthorizationFilter extends AuthorizationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        return false;
    }

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            //no roles specified, so nothing to check - allow access.
            return true;
        }
        //只要有一个就返回true,原来的是hasAllRoles
        Set<String> roles = CollectionUtils.asSet(rolesArray);
        for (String role :
                roles) {
            if (subject.hasRole(role)){
                return true;
            }
        }
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        LinkedHashMap<String,String> map= new LinkedHashMap<>();
        map.put("code", Integer.toString(CommonResult.getRedirectCode()));
        map.put("success","false");
        map.put("message",CommonResult.getRedirectString());
        map.put("data","roles/index.jsp");
        JSONObject json = new JSONObject(map);
        try {
            makeJson.setStatusAndOutputJson(httpServletResponse,json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
