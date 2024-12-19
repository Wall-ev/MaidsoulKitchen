package com.github.wallev.maidsoulkitchen.util.tlm;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;

public interface LevelMaidLong {

    void accept(ServerLevel serverLevel, EntityMaid maid, Long pGameTime);
    boolean test(ServerLevel serverLevel, EntityMaid maid, Long pGameTime);
}
