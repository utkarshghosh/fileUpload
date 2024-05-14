package com.example.demo.exceptions;

public class FileNotFoundException extends RuntimeException{
    public  FileNotFoundException(String message){
        super(message);
    }
    public FileNotFoundException(String message , Throwable th){
        super(message , th);
    }

    public FileNotFoundException( Throwable th){
        super(th);
    }
}
