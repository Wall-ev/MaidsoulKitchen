package com.github.wallev.maidsoulkitchen.mixin.minecraft;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IAbstractFurnaceAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IRecipeExperinceAward;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
public abstract class MixinAbstractFurnace implements IAbstractFurnaceAccessor, IRecipeExperinceAward {
    @Shadow
    @Final
    private Object2IntOpenHashMap<ResourceLocation> recipesUsed;
    @Shadow
    @Final
    private RecipeType<? extends AbstractCookingRecipe> recipeType;

    @Shadow
    public abstract List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel pLevel, Vec3 pPopVec);

    public RecipeType<? extends AbstractCookingRecipe> getRecipeType$tlma() {
        return this.recipeType;
    }

    @Override
    public void awardExperience$tlma(EntityMaid maid) {
        this.getRecipesToAwardAndPopExperience((ServerLevel) maid.level(), maid.position());
        this.recipesUsed.clear();
    }
}
