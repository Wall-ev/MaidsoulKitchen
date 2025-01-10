package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.crockpot;

import com.github.wallev.maidsoulkitchen.handler.rec.AbstractCookRec;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.world.item.Item;

import java.util.List;

public class CrockPotCookingRec extends AbstractCookRec<CrockPotCookingRecipe> {
    public CrockPotCookingRec(CrockPotCookingRecipe rec, List<List<Item>> ingres, List<Item> result) {
        super(rec, ingres, result);
    }
}