package com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ItemAmount;

import java.util.Map;

public class RecDataUse {
    private Map<ItemDefinition, ItemAmount> itemUse;
    private int recipeRepeat;

    public RecDataUse() {
    }

    public Map<ItemDefinition, ItemAmount> getItemUse() {
        return itemUse;
    }

    protected void setItemUse(Map<ItemDefinition, ItemAmount> itemUse) {
        this.itemUse = itemUse;
    }

    public void set(Map<ItemDefinition, ItemAmount> itemUse, int repeat) {
        this.setItemUse(itemUse);
        this.setRecipeRepeat(repeat);
    }

    public int getRecipeRepeat() {
        return recipeRepeat;
    }

    protected void setRecipeRepeat(int recipeRepeat) {
        this.recipeRepeat = recipeRepeat;
    }
}
