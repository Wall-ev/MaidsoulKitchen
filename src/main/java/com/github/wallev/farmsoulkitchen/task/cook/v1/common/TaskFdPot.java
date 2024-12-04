package com.github.wallev.farmsoulkitchen.task.cook.v1.common;

import com.github.wallev.farmsoulkitchen.api.task.v1.cook.IFdPotCook;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.farmsoulkitchen.inventory.container.maid.CookConfigContainer;
import com.github.wallev.farmsoulkitchen.task.cook.handler.v2.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TaskFdPot<B extends BlockEntity, R extends Recipe<? extends Container>> implements ICookTask<B, R>, IFdPotCook<B, R> {
    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, B blockEntity, MaidRecipesManager<R> recManager) {
        return maidShouldMoveTo(serverLevel, maid, blockEntity, recManager);
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, B blockEntity, MaidRecipesManager<R> recManager) {
        maidCookMake(serverLevel, maid, blockEntity, recManager);
    }

    public MenuProvider getTaskConfigGuiProvider(EntityMaid maid) {
        final int entityId = maid.getId();
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Cook Config Container2");
            }

            @Override
            public AbstractContainerMenu createMenu(int index, Inventory playerInventory, Player player) {
                return new CookConfigContainer(index, playerInventory, entityId);
            }
        };
    }
}
