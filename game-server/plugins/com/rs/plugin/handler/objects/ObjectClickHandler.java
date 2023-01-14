package com.rs.plugin.handler.objects;

import com.rs.plugin.eventbus.ObjectClickEvent;
import com.rs.plugin.handler.PluginHandler;
import com.rs.world.Location;
import com.rs.world.ObjectType;

public abstract class ObjectClickHandler extends PluginHandler<ObjectClickEvent> {

    private Location[] tiles;
    private boolean checkDistance = true;
    private ObjectType type;

    public ObjectClickHandler(boolean checkDistance, Object[] namesOrIds, Location... tiles) {
        super(namesOrIds);
        this.tiles = tiles;
        this.checkDistance = checkDistance;
    }

    public ObjectClickHandler(Object[] namesOrIds, Location... tiles) {
        this(true, namesOrIds, tiles);
    }

    public ObjectClickHandler(Object[] namesOrIds) {
        this(true, namesOrIds);
    }

    public ObjectClickHandler(Object[] namesOrIds, ObjectType type) {
        this(true, namesOrIds);
        this.type = type;
    }

    public boolean isCheckDistance() {
        return checkDistance;
    }

    public Location[] getTiles() {
        return tiles;
    }

    public ObjectType getType() {
        return type;
    }
}
