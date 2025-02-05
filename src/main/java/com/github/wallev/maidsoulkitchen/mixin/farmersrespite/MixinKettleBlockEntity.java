package com.github.wallev.maidsoulkitchen.mixin.farmersrespite;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.ICbeAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IRecipeExperinceAward;
import com.github.wallev.maidsoulkitchen.task.cook.v1.farmersrespite.IKettleBlockEntity;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import umpaz.farmersrespite.common.block.entity.KettleBlockEntity;
import umpaz.farmersrespite.common.crafting.KettleRecipe;
import umpaz.farmersrespite.common.registry.FRRecipeTypes;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import java.util.List;
import java.util.Optional;

@Mixin(value = KettleBlockEntity.class, remap = false)
public abstract class MixinKettleBlockEntity extends SyncedBlockEntity implements ICbeAccessor, IRecipeExperinceAward, IKettleBlockEntity {
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

    @Shadow @Final private FluidTank fluidTank;

    @Shadow private ResourceLocation lastRecipeID;

    @Shadow private boolean checkNewRecipe;

    /**
     * 烹饪的厨具内部是否可以烹饪（仅仅是与配方原料相关的判断，不包括外部条件，比如农夫乐事的需要Heated状态）
     *
     * @return 是否可以烹饪
     */
    @Override
    public boolean innerCanCook$tlma() {
        Optional<KettleRecipe> recipe = this.getMatchingRecipe(new RecipeWrapper(this.getInventory()));
        return recipe.isPresent() && this.canBrew(recipe.get(), (KettleBlockEntity) (Object) this);
    }

    @Override
    public void awardExperience$tlma(EntityMaid maid) {
        this.getUsedRecipesAndPopExperience(maid.level, maid.position());
        this.usedRecipeTracker.clear();
    }

    @Override
    public void buildCheckNewRecipe() {
//        RecipeWrapper recipeWrapper = new RecipeWrapper(this.getInventory());
//        List<KettleRecipe> allRecipesFor = this.level.getRecipeManager().getAllRecipesFor((RecipeType) FRRecipeTypes.BREWING.get());
//        allRecipesFor.stream().filter((a) -> {
//            return a.matches(recipeWrapper, this.level) && a.getFluidIn().getFluid().isSame(this.fluidTank.getFluid().getFluid());
//        }).findFirst().ifPresent(kettleRecipe -> {
//            this.lastRecipeID = kettleRecipe.getId();
//        });

        this.checkNewRecipe = true;
    }
}
