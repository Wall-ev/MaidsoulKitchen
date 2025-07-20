package com.github.wallev.maidsoulkitchen.mixin.compat.farmersdelight;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.ICookBeAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@TaskMixin(value = TaskInfo.FD_COOK_POT)
@Mixin(value = CookingPotBlockEntity.class, remap = false)
public abstract class CookingPotBlockEntityMixin implements ICookBeAccessor {
    @Shadow
    @Final
    private Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    @Shadow
    protected abstract Optional<CookingPotRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow
    protected abstract boolean canCook(CookingPotRecipe recipe);

    @Shadow
    public abstract List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos);

    @Shadow
    public abstract ItemStackHandler getInventory();

    @Override
    public boolean kl$canCook() {
        return this.kl$canCook(this.getInventory(), this::getMatchingRecipe, this::canCook);
    }

    @Override
    public void kl$getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
        this.getUsedRecipesAndPopExperience(level, pos);
    }

    @Override
    public Map<ResourceLocation, Integer> kl$usedRecipeTracker() {
        return this.usedRecipeTracker;
    }
}
