package com.github.wallev.maidsoulkitchen.task.cook.v1.yhc;

import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.action.IMaidAction;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Optional;

public class TaskDryingRack implements ICookTask<DryingRackBlockEntity, DryingRackRecipe>, IMaidAction {
    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.YHC_DRYING_RACK;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof DryingRackBlockEntity;
    }

    @Override
    public RecipeType<DryingRackRecipe> getRecipeType() {
        return YHBlocks.RACK_RT.get();
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, DryingRackBlockEntity blockEntity, MaidRecipesManager<DryingRackRecipe> recManager) {
        if (!serverLevel.canSeeSky(blockEntity.getBlockPos()) || !serverLevel.isDay() || serverLevel.isRainingAt(blockEntity.getBlockPos())) {
            return false;
        }
        if (blockEntity.getItems().stream().allMatch(ItemStack::isEmpty) && !recManager.getRecipesIngredients().isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, DryingRackBlockEntity blockEntity, MaidRecipesManager<DryingRackRecipe> recManager) {
        if (!serverLevel.canSeeSky(blockEntity.getBlockPos()) || !serverLevel.isDay() || serverLevel.isRainingAt(blockEntity.getBlockPos())) {
            return;
        }
        Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = recManager.getRecipeIngredient();
        if (blockEntity.getItems().stream().allMatch(ItemStack::isEmpty) && !recipeIngredient.getFirst().isEmpty()) {
            ItemStack itemStack = recipeIngredient.getSecond().get(0).get(0);
            Optional<DryingRackRecipe> cookableRecipe = blockEntity.getCookableRecipe(itemStack);
            if (cookableRecipe.isPresent()) {
                for (int i = 0; i < Math.min(4, itemStack.getCount()); i++) {
                    blockEntity.placeFood(itemStack, cookableRecipe.get().getCookingTime());
                }
                pickupAction(maid);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.YHC_DRYING_RACK.uid;
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.RACK.asStack();
    }
}
