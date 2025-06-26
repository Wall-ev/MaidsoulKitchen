package com.github.wallev.maidsoulkitchen.task.cook.minecraft.furnace;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

@TaskClassAnalyzer(TaskInfo.FURNACE)
public class AbstractCookingRecSerializerManager extends RecSerializerManager<AbstractCookingRecipe> {
    private static final AbstractCookingRecSerializerManager INSTANCE = new AbstractCookingRecSerializerManager();

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected AbstractCookingRecSerializerManager() {
        super((RecipeType) RecipeType.SMELTING);
    }

    public static AbstractCookingRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected void initRecs(Level level) {
        List<MKRecipe<AbstractCookingRecipe>> recs = new ArrayList<>();
        recs.addAll(this.createTypeRecs(level, RecipeType.SMELTING));
        recs.addAll(this.createTypeRecs(level, RecipeType.SMOKING));
        recs.addAll(this.createTypeRecs(level, RecipeType.BLASTING));
        this.recipes = ImmutableList.copyOf(recs);
    }

    @Override
    protected void initFuels() {
        this.fuels = createDefaultFuels();
    }

}
