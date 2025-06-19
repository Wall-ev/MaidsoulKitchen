package com.github.wallev.maidsoulkitchen.mixin.minecraft;

import com.github.wallev.maidsoulkitchen.task.cook.minecraft.furnace.IAbstractFurnaceAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.ICookBeAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Map;

@Mixin(value = AbstractFurnaceBlockEntity.class, remap = true)
public abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity implements IAbstractFurnaceAccessor, ICookBeAccessor {
    @Shadow
    protected NonNullList<ItemStack> items;
    @Shadow
    @Final
    private Object2IntOpenHashMap<ResourceLocation> recipesUsed;
    @Shadow(remap = false)
    @Final
    private RecipeType<? extends AbstractCookingRecipe> recipeType;
    @Shadow
    @Final
    private RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;

    protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Shadow
    public abstract List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel pLevel, Vec3 pPopVec);

    @Shadow
    protected abstract boolean canBurn(RegistryAccess pRegistryAccess, @Nullable Recipe<?> pRecipe, NonNullList<ItemStack> pInventory, int pMaxStackSize);

    @Shadow
    protected abstract boolean isLit();

    @SuppressWarnings("unchecked")
    public RecipeType<AbstractCookingRecipe> tlmk$getRecipeType() {
        return (RecipeType<AbstractCookingRecipe>) this.recipeType;
    }

    @Override
    public boolean tlmk$isLit() {
        return this.isLit();
    }

    @Unique
    public boolean tlmk$innerCanCook() {
        if (level == null) {
            return false;
        }
        Recipe<?> recipe = this.quickCheck.getRecipeFor(this, level).orElse(null);
        if (recipe == null) {
            return false;
        }
        int maxStackSize = this.getMaxStackSize();
        return this.canBurn(level.registryAccess(), recipe, this.items, maxStackSize);
    }

    @Override
    public boolean kl$canCook() {
        return this.tlmk$innerCanCook();
    }

    @Override
    public boolean kl$matchCookState() {
        return this.tlmk$isLit();
    }

    @Override
    public void kl$getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
        this.getRecipesToAwardAndPopExperience((ServerLevel) level, pos);
    }

    @Override
    public Map<ResourceLocation, Integer> kl$usedRecipeTracker() {
        return recipesUsed;
    }
}
