package com.github.wallev.maidsoulkitchen.handler.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.ILittleMaidTask;
import com.github.wallev.maidsoulkitchen.api.task.IDataTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.handler.base.container.AbstractCookBlockEntitySerializer;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.AbstractMaidCookBe;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookContainerSerializerRulesManager;
import com.github.wallev.maidsoulkitchen.handler.task.ai.MaidCookMakeTask;
import com.github.wallev.maidsoulkitchen.handler.task.ai.MaidCookMoveTask;
import com.github.wallev.maidsoulkitchen.handler.task.handler.MaidRecipesManager;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class AbstractTaskCook<MCB extends AbstractMaidCookBe<B, R>, B extends BlockEntity, R extends Recipe<? extends Container>>
        implements ILittleMaidTask, IDataTask<CookData> {
    //@FINAL
    private static List<AbstractCookBlockEntitySerializer<?, ?, ?>> SERIALIZER_RULES;

    public AbstractTaskCook() {
        SERIALIZER_RULES = CookContainerSerializerRulesManager.getSerializerRules(this.getRecipeType());
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        return maid.level.isClientSide ? Collections.emptyList() : createTaskCookBrainTasks(maid);
    }

    /**
     * 创建女仆的烹饪AI任务
     *
     * @param maid 女仆对象
     * @return 女仆的烹饪AI任务
     */
    protected List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createTaskCookBrainTasks(EntityMaid maid) {
        MaidRecipesManager<MCB, B, R> maidRecipesManager = this.createMaidRecipesManager(maid);
        MCB maidCookBe = this.createMaidCookBe(maid, maidRecipesManager);
        MaidCookMoveTask<MCB, B, R> maidCookMoveTask = this.createMaidCookMoveTask(maidRecipesManager, maidCookBe);
        MaidCookMakeTask<MCB, B, R> maidCookMakeTask = this.createMaidCookMakeTask(maidRecipesManager, maidCookBe);
        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(5, maidCookMakeTask));
    }

    /**
     * 获取当前任务所对应的配方类型
     *
     * @return 配方类型
     */
    public abstract RecipeType<R> getRecipeType();

    /**
     * 判断当前方块实体是否是当前任务所对应的厨具方块
     *
     * @param cookBe 方块实体
     * @return 是否是当前任务所对应的方块实体
     */
    public abstract boolean isCookBE(BlockEntity cookBe);

    /**
     * 创建烹饪相关信息，应当同#createBrainTasks的时候一起创建
     *
     * @param maid           女仆对象
     * @param recipesManager 配方管理器
     * @return 返回烹饪相关的信息的创建
     */
    protected abstract MCB createMaidCookBe(EntityMaid maid, MaidRecipesManager<MCB, B, R> recipesManager);

    /**
     * 创建女仆的配方管理器
     *
     * @param maid 女仆对象
     * @return 女仆的配方管理器
     */
    protected MaidRecipesManager<MCB, B, R> createMaidRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, this);
    }

    /**
     * 创建女仆移动到厨具的AI任务
     *
     * @param maidRecipesManager 女仆的配方管理器
     * @param maidCookBe         烹饪相关信息
     * @return 女仆移动到厨具的AI任务
     */
    protected MaidCookMoveTask<MCB, B, R> createMaidCookMoveTask(MaidRecipesManager<MCB, B, R> maidRecipesManager, MCB maidCookBe) {
        return new MaidCookMoveTask<>(this, maidRecipesManager, maidCookBe);
    }

    /**
     * 创建女仆执行烹饪任务的AI任务
     *
     * @param maidRecipesManager 女仆的配方管理器
     * @param maidCookBe         烹饪相关信息
     * @return 女仆执行烹饪任务的AI任务
     */
    protected MaidCookMakeTask<MCB, B, R> createMaidCookMakeTask(MaidRecipesManager<MCB, B, R> maidRecipesManager, MCB maidCookBe) {
        return new MaidCookMakeTask<>(this, maidRecipesManager, maidCookBe);
    }

    /**
     * 判断当前方块实体是否可以执行当前任务
     *
     * @param serverLevel 服务器世界
     * @param maidCookBe  烹饪相关信息
     * @return 是否可以执行当前任务
     */
    public boolean shouldMoveTo(ServerLevel serverLevel, MCB maidCookBe) {
        for (AbstractCookBlockEntitySerializer<?, ?, ?> serializerRule : SERIALIZER_RULES) {
            if (((AbstractCookBlockEntitySerializer<MCB, B, R>) serializerRule).canDoMaidCookBe(maidCookBe)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 执行当前任务
     *
     * @param serverLevel 服务器世界
     * @param maidCookBe  烹饪相关信息
     */
    public void processCookMake(ServerLevel serverLevel, MCB maidCookBe) {
        for (AbstractCookBlockEntitySerializer<?, ?, ?> serializerRule : SERIALIZER_RULES) {
            if (((AbstractCookBlockEntitySerializer<MCB, B, R>) serializerRule).canDoMaidCookBe(maidCookBe)) {
                ((AbstractCookBlockEntitySerializer<MCB, B, R>) serializerRule).doMaidCookBe(maidCookBe);
            }
        }
    }

    /**
     * 获取抵达目的地的最短距离
     *
     * @return 抵达目的地的最短距离
     */
    public double getCloseEnoughDist() {
        return 3.2;
    }

    @Override
    public CookData getDefaultData() {
        return new CookData();
    }
}

