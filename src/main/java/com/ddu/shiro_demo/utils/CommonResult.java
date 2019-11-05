package com.ddu.shiro_demo.utils;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Accessors(chain = true)
@Slf4j
public class CommonResult {

    private final static int SUCCESS_CODE = 200;
    private final static int REDIRECT_CODE = 302;
//    private final static int UNAUTHORIZED_CODE = 401;
//    private final static int NOTFOUND_CODE = 404;
    private final static int FAILURE_CODE = 10000;
    private final static String SUCCESS_STRING = "请求成功";
    private final static String FAILURE_STRING = "请求失败";
    private final static String REDIRECT_STRING = "权限不足或找不到该网站!若您未登录，请先登陆";
    private final static String LOGOUT_STRING = "登出成功";


    private int code;
    private boolean success;
    private String message;
    private Object data;

    public static CommonResult success(Object data) {
        return success().setData(data);
    }

    public static CommonResult success() {
        return new CommonResult()
                .setCode(SUCCESS_CODE)
                .setSuccess(true)
                .setMessage(SUCCESS_STRING);
    }

    public static CommonResult failure(String message) {
        return failure().setMessage(message);
    }

    public static CommonResult failure() {
        return new CommonResult()
                .setCode(FAILURE_CODE)
                .setSuccess(false)
                .setMessage(FAILURE_STRING);
    }

    /**
     * 根据boolean自动选择返回成功/失败实例
     */
    public static CommonResult expect(boolean success) {
        return success ? success() : failure();
    }

    /**
     * 配合@expect（）方法使用
     */
    public CommonResult setSuccessMessage(String message) {
        return isSuccess() ? setMessage(message) : this;
    }

    /**
     * 配合@expect（）方法使用
     */
    public CommonResult setFailureMessage(String message) {
        return !isSuccess() ? setMessage(message) : this;
    }

    /**
     * 配合@expect（）方法使用
     */
    public CommonResult setSuccessData(Object data) {
        return isSuccess() ? setData(data) : this;
    }

    public static int getSuccessCode() {
        return SUCCESS_CODE;
    }

    public static int getRedirectCode() {
        return REDIRECT_CODE;
    }

    public static int getFailureCode() {
        return FAILURE_CODE;
    }

    public static String getSuccessString() {
        return SUCCESS_STRING;
    }

    public static String getFailureString() {
        return FAILURE_STRING;
    }

    public static String getRedirectString() {
        return REDIRECT_STRING;
    }

    public static String getLogoutString() {
        return LOGOUT_STRING;
    }
}
