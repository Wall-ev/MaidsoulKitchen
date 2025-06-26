package com.github.wallev.maidsoulkitchen.mixin.compat.farmersrespite;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.ICookBeAccessor;
import com.github.wallev.maidsoulkitchen.util.classana.TaskMixin;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import umpaz.farmersrespite.common.block.entity.KettleBlockEntity;
import umpaz.farmersrespite.common.crafting.KettleRecipe;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@TaskMixin(task = TaskInfo.FR_KETTLE)
@Mixin(value = KettleBlockEntity.class, remap = false)
public abstract class KettleBlockEntityMixin extends SyncedBlockEntity implements ICookBeAccessor {
    @Shadow
    @Final
    private Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    public KettleBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Shadow
    protected abstract Optional<KettleRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow
    public abstract ItemStackHandler getInventory();

    @Shadow
    protected abstract boolean canBrew(KettleRecipe recipe, KettleBlockEntity kettle);

    @Shadow
    public abstract List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos);

    @Override
    public boolean kl$canCook() {
        return this.kl$canCook(this.getInventory(), this::getMatchingRecipe, r -> this.canBrew(r, this.kl$cast()));
    }

    @Override
    public void kl$getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
        this.getUsedRecipesAndPopExperience(level, pos);
    }

    @Override
    public Map<ResourceLocation, Integer> kl$usedRecipeTracker() {
        return usedRecipeTracker;
    }
}
