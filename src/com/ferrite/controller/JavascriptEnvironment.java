package com.ferrite.controller;

import java.util.HashMap;

public class JavascriptEnvironment {
    private HashMap<String, Object> vars;

    private static JavascriptEnvironment instance;

    private JavascriptEnvironment() {
        this.vars = new HashMap<>();
    }

    public static JavascriptEnvironment getInstance() {
        if (instance == null) {
            instance = new JavascriptEnvironment();
        }
        return instance;
    }

    public Object addPair(String key, Object value) {
        return this.vars.put(key, value);
    }

    public Object removePair(String key) {
        return this.vars.remove(key);
    }

    public boolean keyExists(String key) {
        return this.vars.containsKey(key);
    }

    public Object getValue(String key) {
        return this.vars.get(key);
    }
}
