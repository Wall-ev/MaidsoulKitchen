package com.github.wallev.maidsoulkitchen.task.cook.common.manager;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecsGenerate<R extends Recipe<? extends Container>> {
    public static final int TICK_SCAN_LIMIT = 10;
    protected Map<ItemDefinition, Long> available = new HashMap<>();
    protected List<MKRecipe<R>> rec = new ArrayList<>();
    protected List<MKRecipe<R>> currentRecs = new ArrayList<>();
    private int slots = 0;
    private int lastSlot = 0;

    public List<MKRecipe<R>> tickRun() {
        List<MKRecipe<R>> mkRecipes = currentRecs.subList(lastSlot, Math.min(lastSlot + TICK_SCAN_LIMIT, slots));
        lastSlot += TICK_SCAN_LIMIT;
        return mkRecipes;
    }

    public void markDone() {
        this.lastSlot = slots;
    }

    public boolean done() {
        return lastSlot >= slots;
    }

    public Map<ItemDefinition, Long> getAvailable() {
        return available;
    }

    public void setAvailable(Map<ItemDefinition, Long> available) {
        this.available = available;
    }

    public List<MKRecipe<R>> getRecs() {
        return rec;
    }

    public void setRecs(List<MKRecipe<R>> rec) {
        this.rec = rec;
        this.slots = rec.size();
    }

    public List<MKRecipe<R>> getCurrentRecs() {
        return currentRecs;
    }

    public void setCurrentRecs(List<MKRecipe<R>> currentRecs) {
        this.currentRecs = currentRecs;
    }

    public void clear() {
        this.currentRecs.clear();
        this.available.clear();
        this.lastSlot = 0;
    }
}
