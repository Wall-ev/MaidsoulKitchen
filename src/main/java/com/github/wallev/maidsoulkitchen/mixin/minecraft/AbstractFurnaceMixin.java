package com.github.wallev.maidsoulkitchen.mixin.minecraft;

import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IAbstractFurnaceAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IRecipeExperinceAward;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceMixin implements IAbstractFurnaceAccessor, IRecipeExperinceAward {
    @Shadow
    @Final
    private Object2IntOpenHashMap<ResourceLocation> recipesUsed;
    @Shadow
    @Final
    private RecipeType<? extends AbstractCookingRecipe> recipeType;

    @Shadow
    public abstract List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel pLevel, Vec3 pPopVec);

    public RecipeType<? extends AbstractCookingRecipe> tlmk$getRecipeType() {
        return this.recipeType;
    }

    @Override
    public void tlmk$awardExperience(Entity entity) {
        this.getRecipesToAwardAndPopExperience((ServerLevel) entity.level(), entity.position());
        this.recipesUsed.clear();
    }
}
