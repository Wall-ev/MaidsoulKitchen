package com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;

import java.util.HashMap;
import java.util.Map;

public abstract class IItemDown {
    protected final Map<ItemDefinition, Integer> useItemDef = new HashMap<>();
    protected int useSlot = 0;
    protected int recLimitIndex = 0;

    public void clear() {
        useItemDef.clear();
        useSlot = 0;
        recLimitIndex = 0;
    }

    public abstract boolean read(RecDataUse recDataUse);

    public Map<ItemDefinition, Integer> getUseItemDef() {
        return useItemDef;
    }

    public int getRecLimitIndex() {
        return recLimitIndex;
    }
}
