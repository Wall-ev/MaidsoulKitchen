package com.github.wallev.maidsoulkitchen.compat.msm.farm_and_charm.roaster;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.WorldlyContainerInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.satisfy.farm_and_charm.core.block.entity.RoasterBlockEntity;
import net.satisfy.farm_and_charm.core.registry.EntityTypeRegistry;

@InvHandlerRegister(TaskInfo.MSM_FARM_AND_CHARM_ROASTER)
public class RoasterBlockEntityContainerInvRegister extends WorldlyContainerInvHandlerFactory<RoasterBlockEntity> {

    public RoasterBlockEntityContainerInvRegister() {
        super(EntityTypeRegistry.ROASTER_BLOCK_ENTITY.get());
    }

}
