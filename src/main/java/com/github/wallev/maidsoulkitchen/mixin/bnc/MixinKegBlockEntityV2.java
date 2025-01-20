package com.github.wallev.maidsoulkitchen.mixin.bnc;

import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.ICookBeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import java.util.Optional;

@Mixin(value = KegBlockEntity.class, remap = false)
public abstract class MixinKegBlockEntityV2 extends SyncedBlockEntity implements ICookBeAccessor<KegBlockEntity, KegRecipe> {
    public MixinKegBlockEntityV2(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Shadow public abstract ItemStackHandler getInventory();

    @Shadow protected abstract Optional<KegRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow protected abstract boolean canFerment(KegRecipe recipe, Level level);

    /**
     * 判断厨具内部的原料是否可以烹饪
     * <br>即有符合配方的原料
     * <br>但不会检测额外条件
     * <br>比如：需要燃料，加水等
     *
     * @return 是否可以烹饪
     */
    @Override
    public boolean canCook$msk() {
        RecipeWrapper inventoryWrapper = new RecipeWrapper(this.getInventory());
        Optional<KegRecipe> matchingRecipe = this.getMatchingRecipe(inventoryWrapper);
        return matchingRecipe.isPresent() && this.canFerment(matchingRecipe.get(), this.level);
    }
}
