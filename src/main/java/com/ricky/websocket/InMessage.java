package com.ricky.websocket;

public class InMessage {

    private String name;
    private String id;

    public InMessage() {
    }

    public InMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
