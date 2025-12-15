package com.github.wallev.maidsoulkitchen.compat.msm.farm_and_charm.crafting_bowl;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.WorldlyContainerInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.satisfy.farm_and_charm.core.block.entity.CraftingBowlBlockEntity;
import net.satisfy.farm_and_charm.core.registry.EntityTypeRegistry;

@InvHandlerRegister(TaskInfo.MSM_FARM_AND_CHARM_CRAFTING_BOWL)
public class CraftingBowlBlockEntityContainerInvRegister extends WorldlyContainerInvHandlerFactory<CraftingBowlBlockEntity> {

    public CraftingBowlBlockEntityContainerInvRegister() {
        super(EntityTypeRegistry.CRAFTING_BOWL_BLOCK_ENTITY.get());
    }

}
