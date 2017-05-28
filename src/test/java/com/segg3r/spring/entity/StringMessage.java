package com.segg3r.spring.entity;

import java.io.Serializable;

public class StringMessage implements Serializable {

    private String message;

    public StringMessage() {
    }

    public StringMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
