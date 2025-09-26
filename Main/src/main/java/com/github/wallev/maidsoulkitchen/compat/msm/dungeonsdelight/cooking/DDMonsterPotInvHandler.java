package com.github.wallev.maidsoulkitchen.compat.msm.dungeonsdelight.cooking;

import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.IInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import net.minecraft.core.Direction;
import net.yirmiri.dungeonsdelight.common.block.entity.MonsterPotBlockEntity;
import net.yirmiri.dungeonsdelight.core.registry.DDBlockEntities;
import org.jetbrains.annotations.Nullable;

@InvHandlerRegister(TaskInfo.MSM_DD_MONSTER_POT)
public class DDMonsterPotInvHandler extends IInvHandlerFactory<MonsterPotBlockEntity> {

    public DDMonsterPotInvHandler() {
        super(DDBlockEntities.MONSTER_COOKING_POT.get());
    }

    @Override
    protected IInvHandler create(MonsterPotBlockEntity blockEntity, @Nullable Direction side) {
        return createForFdPot(blockEntity, blockEntity.getInventory(), side);
    }
}
