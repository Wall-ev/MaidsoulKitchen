package com.github.wallev.farmsoulkitchen.util.tlm;

import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;

public interface AttackMaidLivingEntity {

    void accept(IAttackTask task, EntityMaid maid, LivingEntity target);
    boolean test(IAttackTask task, EntityMaid maid, LivingEntity target);

}
