package com.github.wallev.maidsoulkitchen.compat.msm.farm_and_charm.stove;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.WorldlyContainerInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.satisfy.farm_and_charm.core.block.entity.StoveBlockEntity;
import net.satisfy.farm_and_charm.core.registry.EntityTypeRegistry;

@InvHandlerRegister(TaskInfo.MSM_FARM_AND_CHARM_STOVE)
public class StoveBlockEntityContainerInvRegister extends WorldlyContainerInvHandlerFactory<StoveBlockEntity> {

    public StoveBlockEntityContainerInvRegister() {
        super(EntityTypeRegistry.STOVE_BLOCK_ENTITY.get());
    }

}
