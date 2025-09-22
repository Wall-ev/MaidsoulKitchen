package com.github.wallev.maidsoulkitchen.compat.msm.farm_and_charm.cookingpot;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.WorldlyContainerInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.satisfy.farm_and_charm.core.block.entity.CookingPotBlockEntity;
import net.satisfy.farm_and_charm.core.registry.EntityTypeRegistry;

@InvHandlerRegister(TaskInfo.MSM_FARM_AND_CHARM_COOKING_POT)
public class CookingPotBlockEntityContainerInvRegister extends WorldlyContainerInvHandlerFactory<CookingPotBlockEntity> {

    public CookingPotBlockEntityContainerInvRegister() {
        super(EntityTypeRegistry.COOKING_POT_BLOCK_ENTITY.get());
    }

}
