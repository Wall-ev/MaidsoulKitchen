package com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ItemAmount;

import java.util.Map;

public class MaidItemDown extends IItemDown {
    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public boolean read(Map<ItemDefinition, ItemAmount> itemUse) {
        return true;
    }
}
