package com.catbert.tlma.api.task.v2;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 额外的外部烹饪条件
 */
public interface IBeState<B extends BlockEntity> {

    /**
     * 父类统一获取当前厨具是否符合外部烹饪条件
     */
    boolean beStateCanCook(B be);

    interface NeedHeated<B extends BlockEntity> extends IBeState<B> {

        /**
         * 需要热源啥的,农夫乐事的厨锅...
         */
        boolean isHeated(B be);

        @Override
        default boolean beStateCanCook(B be){
            return isHeated(be);
        }

    }

    interface NeedTemperate<B extends BlockEntity> extends IBeState<B> {

        /**
         * 有足够的温度,饮酒作乐...
         */
        boolean hasEnoughTemp(B be);

        @Override
        default boolean beStateCanCook(B be){
            return hasEnoughTemp(be);
        }

    }

    interface NeedWater<B extends BlockEntity> extends IBeState<B> {
        /**
         * 有足够的水,妖怪归家...
         */
        boolean hasEnoughWater(B be);

        @Override
        default boolean beStateCanCook(B be){
            return hasEnoughWater(be);
        }

    }

}
