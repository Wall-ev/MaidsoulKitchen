package com.github.wallev.maidsoulkitchen.mixin.farmersdelight;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IFdCbeAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IRecipeExperinceAward;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.List;
import java.util.Optional;

@Mixin(value = CookingPotBlockEntity.class, remap = false)
public abstract class MixinCookingPotBlockEntity implements IFdCbeAccessor<CookingPotRecipe>, IRecipeExperinceAward {
    @Shadow
    protected abstract Optional<CookingPotRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow
    protected abstract boolean canCook(CookingPotRecipe recipe);

    @Shadow public abstract List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos);

    @Shadow @Final private Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    @Override
    public Optional<CookingPotRecipe> tlmk$getMatchingRecipe(RecipeWrapper inventoryWrapper) {
        return getMatchingRecipe(inventoryWrapper);
    }

    @Override
    public boolean tlmk$canCook(CookingPotRecipe recipe) {
        return canCook(recipe);
    }

    @Override
    public void tlmk$awardExperience(EntityMaid maid) {
        this.getUsedRecipesAndPopExperience(maid.level(), maid.position());
        this.usedRecipeTracker.clear();
    }
}
