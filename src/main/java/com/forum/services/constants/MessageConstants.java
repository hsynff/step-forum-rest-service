package com.forum.services.constants;

public class MessageConstants {
    public static final String ERROR_INTERNAL = "Internal error!";
    public static final String ERROR_DUPLICATE_EMAIL = "Email is already taken";
    public static final String ERROR_TOPIC_NOT_FOUND = "Topic with given id not found";
    public static final String ERROR_USER_NOT_FOUND = "User not found";

    public static int errorCodeOf(String message){
        switch (message){
            case ERROR_DUPLICATE_EMAIL:
                return 100;
            case ERROR_USER_NOT_FOUND:
                return 101;
            case ERROR_TOPIC_NOT_FOUND:
                return 200;
            case ERROR_INTERNAL:
                return 300;
            default:
                return 300;
        }
    }

}
