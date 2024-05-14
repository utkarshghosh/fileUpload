package com.example.demo.exceptions;

public class InvalidDataException extends RuntimeException{

    String message;

    public  InvalidDataException(String message){
        super(message);
    }
   public InvalidDataException(String message , Throwable th){
        super(message , th);
    }

   public InvalidDataException( Throwable th){
        super(th);
    }

}
