package com.ddu.shiro_demo.filter;

import com.ddu.shiro_demo.utils.CommonResult;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.json.JSONObject;
import com.ddu.shiro_demo.utils.makeJson;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        LinkedHashMap<String,String> map= new LinkedHashMap<>();
        map.put("code", Integer.toString(CommonResult.getRedirectCode()));
        map.put("success","false");
        map.put("message",CommonResult.getRedirectString());
        map.put("data","sd/index.jsp");
        JSONObject json = new JSONObject(map);
        makeJson.setStatusAndOutputJson(httpServletResponse,json);
        return false;
    }

}
