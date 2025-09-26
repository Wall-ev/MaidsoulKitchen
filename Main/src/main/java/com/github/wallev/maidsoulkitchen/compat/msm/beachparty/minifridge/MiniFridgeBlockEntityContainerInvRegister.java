package com.github.wallev.maidsoulkitchen.compat.msm.beachparty.minifridge;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.WorldlyContainerInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.satisfy.beachparty.core.block.entity.MiniFridgeBlockEntity;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;

@InvHandlerRegister(TaskInfo.MSM_DO_BEACH_PARTY_MINI_FRIDGE)
public class MiniFridgeBlockEntityContainerInvRegister extends WorldlyContainerInvHandlerFactory<MiniFridgeBlockEntity> {

    public MiniFridgeBlockEntityContainerInvRegister() {
        super(EntityTypeRegistry.MINI_FRIDGE_BLOCK_ENTITY.get());
    }

}
