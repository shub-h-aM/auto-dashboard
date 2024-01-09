package com.mll.automation.dashboard.reporting.exception;

import lombok.Builder;

@Builder
public class UserCustomException extends RuntimeException{
    public UserCustomException(){
        super();
    }
    public UserCustomException(String msg){
        super(msg);
    }
}
