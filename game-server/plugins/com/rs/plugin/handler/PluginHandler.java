package com.rs.plugin.handler;

import com.rs.plugin.PluginEvent;

public abstract class PluginHandler<T extends PluginEvent> {
    protected Object[] keys;

    public PluginHandler(Object[] keys) {
        this.keys = keys;
    }

    public abstract void handle(T e);

    public boolean handleGlobal(T e) {
        return false;
    }

    public Object getObj(T e) {
        return null;
    }

    public Object[] keys() {
        return keys;
    }
}
