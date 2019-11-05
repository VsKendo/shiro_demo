package com.ddu.shiro_demo.utils;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class makeJson {

    //工具类禁止实例化
    private makeJson() {

    }
    public static void setStatusAndOutputJson(HttpServletResponse httpServletResponse,JSONObject json)throws Exception{
        httpServletResponse.setStatus(200);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.println(json);
        out.flush();
        out.close();
    }
}
