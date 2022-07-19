package com.remote.tools.utils;

public class HttpResult {

// 响应的状态码

    private int code;

// 响应的响应体

    private String body;

    public HttpResult(int statusCode, String toString) {
        code=statusCode;
        body=toString;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
