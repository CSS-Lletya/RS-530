package com.rs.plugin.eventbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.plugin.PluginEvent;
import com.rs.plugin.handler.PluginHandler;
import com.rs.plugin.handler.objects.ObjectClickHandler;
import com.xeno.entity.actor.player.Player;
import com.xeno.world.Location;
import com.xeno.world.WorldObject;

public class ObjectClickEvent implements PluginEvent {

    private static Map<Object, Map<Integer, List<ObjectClickHandler>>> METHODS = new HashMap<>();

    private Player player;
    private WorldObject object;
    private int optionId;
    private String option;

    public ObjectClickEvent(Player player, WorldObject object, int optionId) {
        this.player = player;
        this.object = object;
        this.optionId = optionId;
    }

    public static void registerMethod(Class<?> eventType, PluginHandler<? extends PluginEvent> method) {
        ObjectClickHandler handler = (ObjectClickHandler) method;
        for (Object key : handler.keys()) {
            Map<Integer, List<ObjectClickHandler>> locMap = METHODS.get(key);
            if (locMap == null) {
                locMap = new HashMap<>();
                METHODS.put(key, locMap);
            }
            if ((handler.getTiles() == null || handler.getTiles().length <= 0)) {
                List<ObjectClickHandler> methods = locMap.get(0);
                if (methods == null)
                    methods = new ArrayList<>();
                methods.add(handler);
                locMap.put(0, methods);
            } else {
                for (Location tile : handler.getTiles()) {
                    List<ObjectClickHandler> methods = locMap.get(tile.getTileHash());
                    if (methods == null)
                        methods = new ArrayList<>();
                    methods.add(handler);
                    locMap.put(tile.getTileHash(), methods);
                }
            }
        }
    }
    
    public int getOptionId() {
        return optionId;
    }

    public Player getPlayer() {
        return player;
    }

    public WorldObject getObject() {
        return object;
    }

    /**
     * This required object definitions which isn't present.
     * @return
     */
    @Deprecated
    public String getOption() {
        return option;
    }
    
    public int getId() {
    	return object.id;
    }

    @Override
    public List<PluginHandler<? extends PluginEvent>> getMethods() {
        List<PluginHandler<? extends PluginEvent>> valids = new ArrayList<>();
        Map<Integer, List<ObjectClickHandler>> methodMapping = METHODS.get(getObject().id);
        if (methodMapping == null)
            return null;
        List<ObjectClickHandler> methods = methodMapping.get(getObject().getTileHash());
        if (methods == null)
            methods = methodMapping.get(-getObject().getType());
        if (methods == null)
            methods = methodMapping.get(0);
        if (methods == null)
            return null;
        for (ObjectClickHandler method : methods) {
            valids.add(method);
        }
        return valids;
    }   
}
