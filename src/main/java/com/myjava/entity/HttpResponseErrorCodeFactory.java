package com.myjava.entity;

import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.concurrent.RejectedExecutionException;

/**
 * @author tangming
 */
public class HttpResponseErrorCodeFactory {

    public static int createErrorCode(Throwable e) {
        if (e.getClass() == IllegalArgumentException.class || e.getClass() == MissingServletRequestParameterException.class) {
            return HttpResponseCode.ILLEGAL_ARGUMENT;
        } else if (e.getClass() == RejectedExecutionException.class) {
            return HttpResponseCode.MANY_REQUEST;
        } else {
            return HttpResponseCode.UNKNOWN_ERROR;
        }
    }
}
