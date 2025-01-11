package com.github.wallev.maidsoulkitchen.handler.rec;

import com.google.common.collect.Sets;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.Set;

public abstract class AbstractCookRec<R extends Recipe<? extends Container>> {
    protected final R rec;
    protected final List<List<Item>> ingres;
    protected final List<Item> result;
    protected final boolean single;
    protected final Set<Item> validItems;
    public AbstractCookRec(R rec, List<List<Item>> ingres, List<Item> result, boolean single) {
        this.rec = rec;
        this.ingres = ingres;
        this.result = result;
        this.single = single;
        this.validItems = createValidItems();
    }

    protected Set<Item> createValidItems() {
        Set<Item> validItems = Sets.newHashSet();
        for (List<Item> ingre : this.ingres) {
            validItems.addAll(ingre);
        }
        return validItems;
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

    public Set<Item> getValidItems() {
        return validItems;
    }
}