package com.frator.exception;

public class ServerNotFoundException extends RuntimeException{
    public ServerNotFoundException(String message){
        super(message);
    }
}
