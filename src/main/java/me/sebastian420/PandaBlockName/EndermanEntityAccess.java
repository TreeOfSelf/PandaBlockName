package me.sebastian420.PandaBlockName;

import net.minecraft.component.ComponentMap;

public interface EndermanEntityAccess {
    void setItemComponentMap(ComponentMap componentMap);
    ComponentMap getItemComponentMap();
}