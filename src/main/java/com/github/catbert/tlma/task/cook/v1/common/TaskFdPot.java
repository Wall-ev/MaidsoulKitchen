package com.github.catbert.tlma.task.cook.v1.common;

import com.github.catbert.tlma.api.task.v1.cook.IFdPotCook;
import com.github.catbert.tlma.api.task.v1.cook.ICookTask;
import com.github.catbert.tlma.inventory.container.maid.CookConfigContainer;
import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
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
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        return maidShouldMoveTo(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        maidCookMake(serverLevel, entityMaid, blockEntity, maidRecipesManager);
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
