package com.catbert.tlma.api;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface ICompatFarmHandler {

    void setNextHandler(ICompatFarmHandler nextHandler);

    boolean shouldMoveTo(EntityMaid maid, BlockPos cropPos, BlockState cropState);

    boolean canLoad();

    class Builder<T extends ICompatFarmHandler> {
        private T head;
        private T tail;
        public Builder<T> addHandler(T handler) {
            if (!handler.canLoad()) return this;

            if (head == null){
                head = tail = handler;
                return this;
            }

            this.tail.setNextHandler(handler);
            this.tail = handler;
            return this;
        }

        public T build(){
            return this.head;
        }
    }
}
