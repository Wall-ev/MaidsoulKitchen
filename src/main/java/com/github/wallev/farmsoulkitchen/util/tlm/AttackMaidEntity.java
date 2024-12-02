package com.github.wallev.farmsoulkitchen.util.tlm;

import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;

public interface AttackMaidEntity {

    void accept(IAttackTask task, EntityMaid maid, Entity target);
    boolean test(IAttackTask task, EntityMaid maid, Entity target);

}
