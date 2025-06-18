package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.ferment;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import dev.xkmc.l2library.base.tile.BaseTank;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationDummyContainer;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationItemContainer;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationRecipe;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlockEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

import java.util.Optional;

import static dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlock.OPEN;

public class FermentationCookBe extends CookBeBase<FermentationTankBlockEntity> {
    public FermentationCookBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof FermentationTankBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be.items;
    }

    @Override
    public int getIngredientSize() {
        return 9;
    }

    @Override
    public int getResultSlot() {
        return 0;
    }

    @Override
    public FluidStack getFluidStack() {
        return be.fluids.getFluidInTank(0);
    }

    @Override
    public boolean cookStateMatch() {
        return true;
    }

    @Override
    public boolean recMatch() {
        FermentationItemContainer items = be.items;
        BaseTank fluids = be.fluids;
        if (!items.isEmpty() || !fluids.isEmpty()) {
            FermentationDummyContainer cont = new FermentationDummyContainer(items, fluids);
            Optional<FermentationRecipe<?>> opt = serverLevel.getRecipeManager().getRecipeFor((RecipeType) YHBlocks.FERMENT_RT.get(), cont, serverLevel);
            return opt.isPresent();
        }

        return false;
    }

    @Override
    public boolean insertInputs(MaidRec rec, ItemInventory itemInventory) {
        boolean inserted = super.insertInputs(rec, itemInventory);
        if (inserted) {
            serverLevel.setBlockAndUpdate(be.getBlockPos(), be.getBlockState().setValue(OPEN, false));
            return true;
        }
        return false;
    }

    @Override
    public void markChanged() {
        be.notifyTile();
    }
}
