package com.ferrite.controller;

import com.ferrite.dom.exceptions.DOMNodeVariantTypeMismatchException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Value;

import java.util.HashMap;

public class JavascriptEnvironment {
    private HashMap<String, Object> vars;
    private Engine engine;
    private Context context;

    private static JavascriptEnvironment instance;

    private JavascriptEnvironment() {
        this.vars = new HashMap<>();

        Engine engine = Engine.newBuilder()
                .option("engine.WarnInterpreterOnly", "false")
                .build();
        this.engine = engine;
        Context context = Context.newBuilder("js").engine(engine).build();
        context.getBindings("js").putMember("input", Controller.getInstance().getInput());
        context.getBindings("js").putMember("output", Controller.getInstance().getOutput());
        context.getBindings("js").putMember("env", this);
        this.context = context;
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

    public Context getContext() {
        return this.context;
    }
}
