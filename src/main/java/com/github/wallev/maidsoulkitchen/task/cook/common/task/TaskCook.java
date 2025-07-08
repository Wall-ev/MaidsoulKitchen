package com.github.wallev.maidsoulkitchen.task.cook.common.task;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.IDataTask;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.KitchenData;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.init.MkItems;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.CookConfigContainer;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class TaskCook implements IMaidsoulKitchenTask, IDataTask<KitchenData> {

    @Override
    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
        return this.getOrIdleTask(maid).vCreateBrainTasks(maid);
    }

    @Override
    public List<Pair<Integer, VBehaviorControl>> vCreateRideBrainTasks(EntityMaid maid) {
        return this.getOrIdleTask(maid).vCreateRideBrainTasks(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.COOK.getUid();
    }

    @Override
    public ItemStack getIcon() {
        return MkItems.CULINARY_HUB.get().getDefaultInstance();
    }

    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return null;
    }

    @Override
    public TaskDataKey<KitchenData> getCookDataKey() {
        return DataRegister.COOK;
    }

    @Override
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

    @Override
    public KitchenData getDefaultData() {
        return new KitchenData();
    }

    @Override
    public boolean enableLookAndRandomWalk(EntityMaid maid) {
        // 工作中禁止游走
        return !maid.getBrain().hasMemoryValue(MkEntities.WORK_POS.get());
    }

    @Override
    public boolean enableEating(EntityMaid maid) {
//        return false;

        // 工作中禁止吃饭
        return !maid.getBrain().hasMemoryValue(MkEntities.WORK_POS.get());
    }

    public ICookTask<?, ?> getOrIdleTask(EntityMaid maid) {
        KitchenData taskData = getTaskData(maid);
        ResourceLocation kitchenName = taskData.getCookName();
        return CookTaskManager.findTask(kitchenName).orElseGet(() -> {
            taskData.setCookName(TaskInfo.IDLE.getUid());
            return CookTaskManager.getIdleTask();
        });
    }

    public Optional<ICookTask<?, ?>> getTask(EntityMaid maid) {
        KitchenData taskData = getTaskData(maid);
        ResourceLocation kitchenName = taskData.getCookName();
        return CookTaskManager.findTask(kitchenName);
    }
}
