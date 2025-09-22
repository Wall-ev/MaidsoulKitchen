package com.github.wallev.maidsoulkitchen.compat.msm.candlelight.cookingpan;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.WorldlyContainerInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.satisfy.candlelight.core.block.entity.CookingPanBlockEntity;
import net.satisfy.candlelight.core.registry.EntityTypeRegistry;

@InvHandlerRegister(TaskInfo.MSM_CANDLE_LINGHT_ROAST)
public class CookingPanBlockEntityContainerInvRegister extends WorldlyContainerInvHandlerFactory<CookingPanBlockEntity> {

    public CookingPanBlockEntityContainerInvRegister() {
        super(EntityTypeRegistry.COOKING_PAN_BLOCK_ENTITY.get());
    }

}
