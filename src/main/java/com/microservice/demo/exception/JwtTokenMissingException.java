package com.microservice.demo.exception;

import org.apache.http.auth.AuthenticationException;

/**
 * Created by: Tharindu Eranga
 * Date: 28 May 2022
 **/
public class JwtTokenMissingException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public JwtTokenMissingException(String msg) {
        super(msg);
    }

}