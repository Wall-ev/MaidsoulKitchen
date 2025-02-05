package com.github.wallev.maidsoulkitchen.mixin.farmersrespite;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.ICbeAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IRecipeExperinceAward;
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
import java.util.Optional;

@Mixin(value = KettleBlockEntity.class, remap = false)
public abstract class MixinKettleBlockEntity extends SyncedBlockEntity implements ICbeAccessor, IRecipeExperinceAward {
    public MixinKettleBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Shadow
    protected abstract Optional<KettleRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow
    public abstract ItemStackHandler getInventory();

    @Shadow
    protected abstract boolean canBrew(KettleRecipe recipe, KettleBlockEntity kettle);

    @Shadow public abstract List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos);

    @Shadow @Final private Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    @Override
    public boolean tlmk$innerCanCook() {
        Optional<KettleRecipe> recipe = this.getMatchingRecipe(new RecipeWrapper(this.getInventory()));
        return recipe.isPresent() && this.canBrew(recipe.get(), (KettleBlockEntity) (Object) this);
    }

    @Override
    public void tlmk$awardExperience(EntityMaid maid) {
        this.getUsedRecipesAndPopExperience(maid.level, maid.position());
        this.usedRecipeTracker.clear();
    }
}
