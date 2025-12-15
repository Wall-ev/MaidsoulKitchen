package com.github.wallev.maidsoulkitchen.compat.msm.baker.cookingpot;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.WorldlyContainerInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.satisfy.bakery.core.block.entity.SmallCookingPotBlockEntity;
import net.satisfy.bakery.core.registry.EntityTypeRegistry;

@InvHandlerRegister(TaskInfo.MSM_DO_BEKERY_SMALL_COOKING_POT)
public class SmallCookingPotBlockEntityContainerInvRegister extends WorldlyContainerInvHandlerFactory<SmallCookingPotBlockEntity> {

    public SmallCookingPotBlockEntityContainerInvRegister() {
        super(EntityTypeRegistry.SMALL_COOKING_POT_BLOCK_ENTITY.get());
    }

}
