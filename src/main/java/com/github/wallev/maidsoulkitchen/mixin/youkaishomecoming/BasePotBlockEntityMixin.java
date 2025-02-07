package com.github.wallev.maidsoulkitchen.mixin.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IFdCbeAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IRecipeExperinceAward;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

@Mixin(value = BasePotBlockEntity.class, remap = false)
public abstract class BasePotBlockEntityMixin implements IFdCbeAccessor<BasePotRecipe>, IRecipeExperinceAward {

    @Shadow
    protected abstract Optional<? extends BasePotRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow
    protected abstract boolean canCook(BasePotRecipe recipe);

    @Shadow public abstract List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos);

    @Shadow @Final private Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    @Override
    @SuppressWarnings("unchecked")
    public Optional<BasePotRecipe> tlmk$getMatchingRecipe(RecipeWrapper inventoryWrapper) {
        return (Optional<BasePotRecipe>) getMatchingRecipe(inventoryWrapper);
    }

    @Override
    public boolean tlmk$canCook(BasePotRecipe recipe) {
        return canCook(recipe);
    }

    @Override
    public void tlmk$awardExperience(Entity entity) {
        this.getUsedRecipesAndPopExperience(entity.level(), entity.position());
        this.usedRecipeTracker.clear();
    }
}
