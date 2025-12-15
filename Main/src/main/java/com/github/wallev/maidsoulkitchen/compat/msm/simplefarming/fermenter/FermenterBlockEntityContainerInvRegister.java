package com.github.wallev.maidsoulkitchen.compat.msm.simplefarming.fermenter;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.WorldlyContainerInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import dev.enemeez.simplefarming.common.block.entity.FermenterBlockEntity;
import dev.enemeez.simplefarming.common.registries.ModBlockEntities;

@InvHandlerRegister(TaskInfo.MSM_SIMPLE_FARMING_FERMENTER)
public class FermenterBlockEntityContainerInvRegister extends WorldlyContainerInvHandlerFactory<FermenterBlockEntity> {

    public FermenterBlockEntityContainerInvRegister() {
        super(ModBlockEntities.FERMENTER.get());
    }

}
