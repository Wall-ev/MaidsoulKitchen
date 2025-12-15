package com.github.wallev.maidsoulkitchen.compat.msm.candlelight.cookingpot;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.WorldlyContainerInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.satisfy.candlelight.core.block.entity.LargeCookingPotBlockEntity;
import net.satisfy.candlelight.core.registry.EntityTypeRegistry;

@InvHandlerRegister(TaskInfo.MSM_CANDLE_LINGHT_COOKING_POT)
public class LargeCookingPotBlockEntityContainerInvRegister extends WorldlyContainerInvHandlerFactory<LargeCookingPotBlockEntity> {

    public LargeCookingPotBlockEntityContainerInvRegister() {
        super(EntityTypeRegistry.COOKING_POT_BLOCK_ENTITY.get());
    }

}
