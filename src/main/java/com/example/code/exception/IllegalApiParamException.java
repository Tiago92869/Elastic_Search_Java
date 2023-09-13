package com.example.code.exception;

public class IllegalApiParamException extends RuntimeException{

    public IllegalApiParamException(String s){
        super(s);
    }
}
