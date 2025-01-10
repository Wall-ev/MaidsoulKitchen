package com.github.wallev.maidsoulkitchen.handler.rec;

import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public abstract class AbstractCookRec<R extends Recipe<? extends Container>> {
    protected final R rec;
    protected final List<List<Item>> ingres;
    protected final List<Item> result;
    protected final boolean single;
    public AbstractCookRec(R rec, List<List<Item>> ingres, List<Item> result, boolean single) {
        this.rec = rec;
        this.ingres = ingres;
        this.result = result;
        this.single = single;
    }

    public AbstractCookRec(R rec, List<List<Item>> ingres, List<Item> result) {
        this(rec, ingres, result, false);
    }

    public R getRec() {
        return rec;
    }

    public List<List<Item>> getIngres() {
        return ingres;
    }

    public List<Item> getResult() {
        return result;
    }

    public boolean isSingle() {
        return single;
    }
}