package com.kevin.devilboard.devl;

import java.util.Map;

public class RxEvent {
    private String name;
    private int code;
    private Map<String, Object> payload;
    private Map<String, String> stringPayload;

    public RxEvent(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public RxEvent(String name, int code, Map<String, Object> payload) {
        this.name = name;
        this.code = code;
        this.payload = payload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public Map<String, String> getStringPayload() {
        return stringPayload;
    }

    public void setStringPayload(Map<String, String> stringPayload) {
        this.stringPayload = stringPayload;
    }
}
