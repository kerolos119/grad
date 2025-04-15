package org.example.exception;

public class ReminderNotFoundException extends RuntimeException{
    public ReminderNotFoundException (String message){
        super(message);
    }
}
