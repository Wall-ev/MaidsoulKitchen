package com.github.wallev.maidsoulkitchen.api.task.cook;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.github.wallev.maidsoulkitchen.api.event.MaidMkTaskEnableEvent;
import com.github.wallev.maidsoulkitchen.api.task.IDataTask;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.compat.patchouli.entry.TaskBookEntryType;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.TaskRegister;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.CookConfigContainer;
import com.github.wallev.maidsoulkitchen.task.cook.common.ai.MaidCookMakeTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.ai.MaidCookMoveTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBe;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class ICookTask<B extends BlockEntity, R extends Recipe<? extends Container>> implements IMaidsoulKitchenTask, IDataTask<CookData> {
    public static final float MOVE_SPEED = 0.5f;
    public static final int VERTICAL_SEARCH_RANGE = 2;
    private static final Map<ResourceLocation, ICookTask<?, ?>> TASK = new HashMap<>();
    public final CookBe.Builder<B> cookBeBuilder;
    public final AbstractCookRule<B, R> cookRule;
    public final RecSerializerManager<R> recSerializerManager;
    public ICookTask() {
        this.cookBeBuilder = this.createCookBeBuilder();
        this.cookRule = this.createCookRule();
        this.recSerializerManager = this.createRecSerializerManager();
    }

    /**
     * 此处由 {@link TaskRegister#init(TaskManager)} 自动添加
     */
    public static void putTask(ResourceLocation id, ICookTask<?, ?> task) {
        TASK.put(id, task);
    }

    public static ICookTask<?, ?> getTask(ResourceLocation id) {
        return TASK.get(id);
    }

    public static BlockPos getSearchPos(EntityMaid maid) {
        return maid.hasRestriction() ? maid.getRestrictCenter() : maid.blockPosition().below();
    }

    public static boolean checkOwnerPos(EntityMaid maid, BlockPos mutableBlockPos) {
        if (maid.isHomeModeEnable()) {
            return true;
        }
        return maid.getOwner() != null && mutableBlockPos.closerToCenterThan(maid.getOwner().position(), 8);
    }

    @Override
    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
        CookBeBase<B> cookBe = this.createCookBe(maid);
        AbstractCookRule<B, R> rule = this.cookRule.getOrCreate();
        MaidRecipesManager2<R> rm = this.createRecipesManager(maid, cookBe);
        MaidCookMoveTask<B, R> cookMove = this.createMaidCookMoveTask(cookBe, rm, rule);
        MaidCookMakeTask<B, R> cookMake = this.createMaidCookMakeTask(cookBe, rm, rule);
        return Lists.newArrayList(Pair.of(5, cookMove), Pair.of(6, cookMake));
    }

    protected MaidCookMoveTask<B, R> createMaidCookMoveTask(CookBeBase<B> cookBe, MaidRecipesManager2<R> rm, AbstractCookRule<B, R> rule) {
        return new MaidCookMoveTask<>(this, rm, rule, cookBe);
    }

    protected MaidCookMakeTask<B, R> createMaidCookMakeTask(CookBeBase<B> cookBe, MaidRecipesManager2<R> rm, AbstractCookRule<B, R> rule) {
        return new MaidCookMakeTask<>(this, rm, rule, cookBe);
    }

    protected CookBe.Builder<B> createCookBeBuilder() {
        return CookBe.Builder.empty();
    }

    protected abstract AbstractCookRule<B, R> createCookRule();

    protected abstract RecSerializerManager<R> createRecSerializerManager();

    protected abstract CookBeBase<B> createCookBe(EntityMaid maid);

    protected MaidRecipesManager2<R> createRecipesManager(EntityMaid maid, CookBeBase<B> cookBe) {
        return new MaidRecipesManager2<>(recSerializerManager, maid, this, cookBe);
    }

    public List<MKRecipe<R>> getRecipes(Level level) {
        return recSerializerManager.getRecipes(level);
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_FURNACE.get(), 0.5f);
    }

    public double getCloseEnoughDist() {
        return 3.2;
    }

    public List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc(EntityMaid maid) {
        return getEventOrElseData(maid, MaidMkTaskEnableEvent::getEnableConditionDesc,
                Lists.newArrayList(Pair.of("has_enough_favor", this::hasEnoughFavor)));
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        return getEventOrElseData(maid, MaidMkTaskEnableEvent::isEnable, this.hasEnoughFavor(maid));
    }

    @Override
    public MenuProvider getTaskConfigGuiProvider(EntityMaid maid) {
        final int entityId = maid.getId();
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Cook Config Container2");
            }

            @Override
            public AbstractContainerMenu createMenu(int index, Inventory playerInventory, Player player) {
                return new CookConfigContainer(index, playerInventory, entityId);
            }
        };
    }

    protected boolean hasEnoughFavor(EntityMaid maid) {
        return maid.getFavorabilityManager().getLevel() >= 1;
    }

    @Override
    public TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.COOK;
    }

    @Override
    public CookData getDefaultData() {
        return new CookData();
    }

    @OnlyIn(Dist.CLIENT)
    public List<Component> getWarnComponent() {
        return Collections.emptyList();
    }

    @Override
    public boolean enableLookAndRandomWalk(EntityMaid maid) {
        // 工作中禁止游走
        return !maid.getBrain().hasMemoryValue(MkEntities.WORK_POS.get());
    }

    @Override
    public boolean enableEating(EntityMaid maid) {
        // 工作中禁止吃饭
        return !maid.getBrain().hasMemoryValue(MkEntities.WORK_POS.get());
    }
}
