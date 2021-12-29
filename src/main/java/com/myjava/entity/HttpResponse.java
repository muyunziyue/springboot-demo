package com.myjava.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tangming
 */
public class HttpResponse<T> {

    public static final String SUCCESS = "S";
    public static final String FAIL = "F";
    public static final String TIMEOUT = "T";
    private String status = SUCCESS;
    private String msg;
    private T data;
    private int errorCode;

    @JsonIgnore
    private Map params = new HashMap();

    public static HttpResponse create() {
        HttpResponse response = new HttpResponse();
        response.setStatus(SUCCESS);
        response.setErrorCode(HttpResponseCode.SUCCESS);
        return response;
    }

    public static HttpResponse createFailed() {
        HttpResponse response = new HttpResponse();
        response.setStatus(FAIL);
        response.setErrorCode(HttpResponseCode.FAILED);
        return response;
    }

    public static HttpResponse createFailed(String msg) {
        HttpResponse response = new HttpResponse();
        response.setMsg(msg);
        response.setStatus(FAIL);
        response.setErrorCode(HttpResponseCode.FAILED);
        return response;
    }

    public static HttpResponse create(Throwable e) {
        return doCreate(e.getMessage(), e);
    }

    public static HttpResponse create(String errorMsg, Throwable e) {
        return doCreate(errorMsg, e);
    }

    private static HttpResponse doCreate(String errorMsg, Throwable e) {
        HttpResponse response = new HttpResponse();
        response.setStatus(FAIL);
        response.setErrorCode(HttpResponseErrorCodeFactory.createErrorCode(e));
        response.setMsg(errorMsg);
        return response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Map getParams(){
        return params;
    }

    public void setParams(Map params){
        this.params = params;
    }

    public void addParam(Object key, Object value){
        params.put(key, value);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
