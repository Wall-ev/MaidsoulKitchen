package com.github.wallev.maidsoulkitchen.api.task.cook;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.github.wallev.maidsoulkitchen.api.task.IDataTask;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.compat.patchouli.entry.TaskBookEntryType;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.TaskRegister;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.CookConfigContainer;
import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.ai.*;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBe;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.TickCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
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

import java.util.*;
import java.util.function.Predicate;

public abstract class ICookTask<B extends BlockEntity, R extends Recipe<? extends Container>> implements IMaidsoulKitchenTask, IDataTask<CookData> {
    public static final float MOVE_SPEED = 0.5f;
    public static final int VERTICAL_SEARCH_RANGE = 2;
    private static final Map<ResourceLocation, ICookTask<?, ?>> TASK = new LinkedHashMap<>();
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

    public static ICookTask<?, ?> getTask(MaidsoulKitchenTask taskInfo) {
        return getTask(taskInfo.uid);
    }

    public static BlockPos getSearchPos(EntityMaid maid, @Nullable BlockPos currentWorkPos) {
        if (currentWorkPos != null && maid.isWithinRestriction(currentWorkPos)) {
            return currentWorkPos;
        }
        return maid.hasRestriction() ? maid.getRestrictCenter() : maid.blockPosition().below();
    }

    public static BlockPos getSearchPos(EntityMaid maid) {
        return getSearchPos(maid, null);
    }

    public static boolean checkOwnerPos(EntityMaid maid, BlockPos mutableBlockPos) {
        if (maid.isHomeModeEnable()) {
            return true;
        }
        return maid.getOwner() != null && mutableBlockPos.closerToCenterThan(maid.getOwner().position(), 8);
    }

    @Override
    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
        MemoryUtil.resetCookWorkState(maid);
        List<Pair<Integer, VBehaviorControl>> controlTasks = new ArrayList<>();

        CookBeBase<B> cookBe = this.createCookBe(maid);
        AbstractCookRule<B, R> rule = this.cookRule.getOrCreate();
        MaidCookManager<R> cm = this.createRecipesManager(maid, cookBe);
        CollectChestIngredientsTask<R> collectChestIngredientsTask = new CollectChestIngredientsTask<>(cm);
        GenerateRecsTask<R> generateRecsTask = new GenerateRecsTask<>(cm);
        CookMoveTask<B, R> cookMoveTask = this.createMaidCookMoveTask(cookBe, cm, rule);
        CookMakeTask<B, R> cookMakeTask = this.createMaidCookMakeTask(cookBe, cm, rule);
        ResetCookMemoryTask<R> resetCookMemoryTask = new ResetCookMemoryTask<>(cm);

        controlTasks.add(Pair.of(0, resetCookMemoryTask));
        controlTasks.add(Pair.of(5, collectChestIngredientsTask));
        controlTasks.add(Pair.of(5, generateRecsTask));
        controlTasks.add(Pair.of(5, cookMoveTask));
        controlTasks.add(Pair.of(6, cookMakeTask));
        controlTasks.add(Pair.of(5, collectChestIngredientsTask));
        if (rule instanceof TickCookRule<B, R>) {
            CookMakePathingTask<B> cookMakePathingTask = new CookMakePathingTask<>(cookBe);
            controlTasks.add(Pair.of(7, cookMakePathingTask));
        }
        return controlTasks;
    }

    protected CookMoveTask<B, R> createMaidCookMoveTask(CookBeBase<B> cookBe, MaidCookManager<R> rm, AbstractCookRule<B, R> rule) {
        return new CookMoveTask<>(this, rm, rule, cookBe);
    }

    protected CookMakeTask<B, R> createMaidCookMakeTask(CookBeBase<B> cookBe, MaidCookManager<R> rm, AbstractCookRule<B, R> rule) {
        return new CookMakeTask<>(this, rm, rule, cookBe);
    }

    protected CookBe.Builder<B> createCookBeBuilder() {
        return CookBe.Builder.empty();
    }

    protected abstract AbstractCookRule<B, R> createCookRule();

    protected abstract RecSerializerManager<R> createRecSerializerManager();

    protected abstract CookBeBase<B> createCookBe(EntityMaid maid);

    protected MaidCookManager<R> createRecipesManager(EntityMaid maid, CookBeBase<B> cookBe) {
        return new MaidCookManager<>(recSerializerManager, maid, this, cookBe);
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
        return Lists.newArrayList(Pair.of("has_enough_favor", this::hasEnoughFavor));
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        return this.hasEnoughFavor(maid);
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
//        return false;

        // 工作中禁止吃饭
        return !maid.getBrain().hasMemoryValue(MkEntities.WORK_POS.get());
    }
}
