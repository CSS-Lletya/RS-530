package com.rs.plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.rs.plugin.handler.PluginHandler;
import com.xeno.utility.LogUtility;
import com.xeno.utility.LogUtility.LogType;

public class PluginMethodRepository {

    public void addMappedHandler(Class<?> eventType, PluginHandler<? extends PluginEvent> method) {
        try {
            eventType.getMethod("registerMethod", Class.class, PluginHandler.class).invoke(null, eventType, method);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            System.err.println("Error registering method for event type: " + eventType.getName() + " from registering method");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public boolean handle(PluginEvent event) {
        PluginHandler<PluginEvent> method = (PluginHandler<PluginEvent>) event.getMethod();
        if (method != null) {
            try {
                method.handle(event);
                return true;
            } catch (Exception e) {
            	LogUtility.log(LogType.ERROR, e.toString());
                return false;
            }
        } else {
            List<PluginHandler<? extends PluginEvent>> methods = event.getMethods();
            if (methods == null || methods.size() <= 0)
                return false;
            for (PluginHandler<? extends PluginEvent> m : methods) {
                try {
                    PluginHandler<PluginEvent> pHandle = (PluginHandler<PluginEvent>) m;
                    pHandle.handle(event);
                } catch (Exception e) {
                	LogUtility.log(LogType.ERROR, e.toString());
                    return false;
                }
            }
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    public Object getObj(PluginEvent event) {
        Object obj = null;
        PluginHandler<PluginEvent> method = (PluginHandler<PluginEvent>) event.getMethod();
        if (method != null) {
            try {
                obj = method.getObj(event);
                if (obj != null)
                    return obj;
            } catch (Exception e) {
            	LogUtility.log(LogType.ERROR, e.toString());
                return null;
            }
        } else {
            List<PluginHandler<? extends PluginEvent>> methods = event.getMethods();
            if (methods == null || methods.size() <= 0)
                return null;
            for (PluginHandler<? extends PluginEvent> m : methods) {
                try {
                    PluginHandler<PluginEvent> pHandle = (PluginHandler<PluginEvent>) m;
                    obj = pHandle.getObj(event);
                } catch (Exception e) {
                	LogUtility.log(LogType.ERROR, e.toString());
                    return null;
                }
            }
            return obj;
        }
        return null;
    }
}
