package com.rs.plugin;

import com.rs.plugin.handler.PluginHandler;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public interface PluginEvent {

    public default PluginHandler<? extends PluginEvent> getMethod() {
        return null;
    }

    public default ObjectArrayList<PluginHandler<? extends PluginEvent>> getMethods() {
        return null;
    }
}
