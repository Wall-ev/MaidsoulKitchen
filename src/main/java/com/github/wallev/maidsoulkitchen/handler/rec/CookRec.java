package com.github.wallev.maidsoulkitchen.handler.rec;

import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public class CookRec<R extends Recipe<? extends Container>> extends AbstractCookRec<R> {
    public CookRec(R rec, List<List<Item>> ingres, List<Item> result, boolean single) {
        super(rec, ingres, result, single);
    }

    public CookRec(R rec, List<List<Item>> ingres, List<Item> result) {
        super(rec, ingres, result);
    }
}