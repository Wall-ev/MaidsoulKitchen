package com.github.wallev.maidsoulkitchen.mixin.farmersrespite;

import com.farmersrespite.common.block.entity.KettleBlockEntity;
import com.farmersrespite.common.crafting.KettleRecipe;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.ICbeAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IRecipeExperinceAward;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import java.util.Optional;

@Mixin(value = KettleBlockEntity.class, remap = false)
public abstract class KettleBlockEntityMixin extends SyncedBlockEntity implements ICbeAccessor, IRecipeExperinceAward {
    @Shadow
    @Final
    private Object2IntOpenHashMap<ResourceLocation> experienceTracker;

    public KettleBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Shadow
    private static void splitAndSpawnExperience(Level level, Vec3 pos, int craftedAmount, float experience) {
    }

    @Shadow
    protected abstract Optional<KettleRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow
    public abstract ItemStackHandler getInventory();

    @Shadow
    protected abstract boolean canBrew(KettleRecipe recipe);

    @Override
    public boolean tlmk$innerCanCook() {
        Optional<KettleRecipe> recipe = this.getMatchingRecipe(new RecipeWrapper(this.getInventory()));
        return recipe.isPresent() && this.canBrew(recipe.get());
    }

    @Override
    public void tlmk$awardExperience(Entity entity) {
        this.experienceTracker.forEach((resourceLocation, value) -> {
            entity.level.getRecipeManager().byKey(resourceLocation).ifPresent(recipe -> {
                splitAndSpawnExperience(entity.level, entity.position(), value, ((KettleRecipe) recipe).getExperience());
            });
        });
        this.experienceTracker.clear();
    }
}
