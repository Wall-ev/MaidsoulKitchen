package com.github.wallev.maidsoulkitchen.compat.msm.meadow.cheese_form;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.WorldlyContainerInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.satisfy.meadow.core.block.entity.CheeseFormBlockEntity;
import net.satisfy.meadow.core.registry.EntityTypeRegistry;

@InvHandlerRegister(TaskInfo.MSM_MEADOW_CHEESE_FORM)
public class CheeseFormBlockEntityContainerInvRegister extends WorldlyContainerInvHandlerFactory<CheeseFormBlockEntity> {

    public CheeseFormBlockEntityContainerInvRegister() {
        super(EntityTypeRegistry.CHEESE_FORM_BLOCK_ENTITY.get());
    }

}
