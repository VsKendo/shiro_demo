package com.ddu.shiro_demo.filter;

import com.ddu.shiro_demo.utils.CommonResult;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;
import org.json.JSONObject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import com.ddu.shiro_demo.utils.makeJson;
import java.util.LinkedHashMap;

public class MyLogoutFilter extends LogoutFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //try/catch added for SHIRO-298:
        try {
            subject.logout();
        } catch (SessionException ise) {
            System.out.println("Encountered session exception during logout.  This can generally safely be ignored.");
        }
        LinkedHashMap<String,String> map= new LinkedHashMap<>();
        map.put("code", Integer.toString(CommonResult.getRedirectCode()));
        map.put("success","true");
        map.put("message",CommonResult.getLogoutString());
        map.put("data","sd/index.jsp");
        JSONObject json = new JSONObject(map);
        makeJson.setStatusAndOutputJson(httpServletResponse,json);
        return false;
    }

}
