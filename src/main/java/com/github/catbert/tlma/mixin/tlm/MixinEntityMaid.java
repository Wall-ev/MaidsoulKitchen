package com.github.catbert.tlma.mixin.tlm;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.catbert.tlma.config.subconfig.TaskConfig;
import com.github.catbert.tlma.entity.passive.CookTaskData;
import com.github.catbert.tlma.util.FakePlayerUtil;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(value = EntityMaid.class, remap = false)
public abstract class MixinEntityMaid extends TamableAnimal implements CrossbowAttackMob, IMaid, IAddonMaid {
    @Unique
    @SuppressWarnings("all")
    private static final EntityDataAccessor<CompoundTag> MAID_ADDON_DATA = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.COMPOUND_TAG);
    @Unique
    @SuppressWarnings("all")
    private static final EntityDataAccessor<Integer> SEARCHY_OFFSET_DATA = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.INT);
    @Unique
    @SuppressWarnings("all")
    private static final String MAID_ADDON_TAG = "MaidAddonData";
    @Unique
    @SuppressWarnings("all")
    private static final String SEARCHY_OFFSET_TAG = "SearchYOffset";
    @Unique
    private static final String COOK_TASK = "CookTask";
    private static final String RECIPE = "recipe";
    private static final String MODE = "mode";
    @Shadow
    @Final
    public static EntityType<EntityMaid> TYPE;
    @Unique
    @SuppressWarnings("all")
    private WeakReference<FakePlayer> fakePlayer;
    @Unique
    private CookTaskData cookTaskData;
    @Shadow
    private IMaidTask task;

    protected MixinEntityMaid(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public abstract IMaidTask getTask();

    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V")
    private void init$tlma(CallbackInfo ci) {
        this.cookTaskData = new CookTaskData();
    }

    @Inject(at = @At("TAIL"), remap = true, method = "defineSynchedData()V")
    private void registerData$tlma(CallbackInfo ci) {
//        CompoundTag compoundTag = new CompoundTag();
//        compoundTag.putInt(SEARCHY_OFFSET_TAG, 4);


//        CompoundTag compoundTag = new CompoundTag();
//        TaskManager.getTaskMap().forEach((uid, task) -> {
//            if (task instanceof ICookTask<?,?> cookTask) {
//                CompoundTag taskRuleTag = new CompoundTag();
//                taskRuleTag.putString(MODE, "random");
//                taskRuleTag.put(RECIPE, new ListTag());
//                compoundTag.put(uid.getPath(), taskRuleTag);
//            }
//        });
//        CompoundTag cookTaskTag = new CompoundTag();
//        cookTaskTag.put(COOK_TASK, compoundTag);
//        entityData.define(MAID_ADDON_DATA, cookTaskTag);
//
        entityData.define(SEARCHY_OFFSET_DATA, TaskConfig.SEARCHY_OFFSET.get());
    }

    @Inject(at = @At("TAIL"), remap = true, method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V")
    private void writeAdditional$tlma(CompoundTag compoundNBT, CallbackInfo ci) {
//        CompoundTag addonMaidDat = getAddonMaidData$tlma();
//        if (addonMaidDat != null) {
//            compoundNBT.put(MAID_ADDON_TAG, addonMaidDat);
//        }
//
        compoundNBT.putInt(SEARCHY_OFFSET_TAG, this.entityData.get(SEARCHY_OFFSET_DATA));

        this.cookTaskData.save(compoundNBT);
    }

    @Inject(at = @At("TAIL"), remap = true, method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V")
    private void readAdditional$tlma(CompoundTag compoundNBT, CallbackInfo ci) {
//        if (compoundNBT.contains(MAID_ADDON_TAG)) {
//            setAddonMaidData$tlma(compoundNBT.getCompound(MAID_ADDON_TAG));
//        }
//
        if (compoundNBT.contains(SEARCHY_OFFSET_TAG, Tag.TAG_INT)) {
            this.entityData.set(SEARCHY_OFFSET_DATA, compoundNBT.getInt(SEARCHY_OFFSET_TAG));
        }
//
        this.cookTaskData.load(compoundNBT);
    }

    @Override
    public CompoundTag getAddonMaidData$tlma() {
        return entityData.get(MAID_ADDON_DATA);
    }

    @Override
    public void setAddonMaidData$tlma(CompoundTag nbt) {
        this.entityData.set(MAID_ADDON_DATA, nbt);
    }

    @Override
    public Integer getStartYOffset$tlma() {
        return this.entityData.get(SEARCHY_OFFSET_DATA);
//        ??为啥在MaidOverlay不能实时获取呢？
//        return getAddonMaidData().getInt(SEARCHY_OFFSET_TAG);
    }

    @Override
    public void setStartYOffset$tlma(int offset) {
        this.entityData.set(SEARCHY_OFFSET_DATA, offset);
//        getAddonMaidData().putInt(SEARCHY_OFFSET_TAG, offset);
    }

    @Override
    public void addOrRemoveRecipe(String recipeId) {
        ListTag recipeListTag = getRecipeListTag();
        StringTag recipe = StringTag.valueOf(recipeId);
        if (recipeListTag.contains(recipe)) {
            recipeListTag.remove(recipe);
        } else if (recipeListTag.size() <= TaskConfig.COOK_SELECTED_RECIPES.get()) {
            recipeListTag.add(recipe);
        }
        getCookTaskTag().put(RECIPE, recipeListTag);
    }

    @Override
    public @NotNull WeakReference<FakePlayer> getFakePlayer$tlma() {
        return fakePlayer;
    }

    @Override
    public void initFakePlayer$tlma() {
        if (fakePlayer == null) {
            this.fakePlayer = FakePlayerUtil.setupBeforeTrigger((ServerLevel) level(), this.getName().getString(), this);
        }
    }

    public boolean openMaidGuiFromSideTab(Player player, int tabIndex, boolean simulate) {
        if (player instanceof ServerPlayer && !this.isSleeping()) {
            this.navigation.stop();
            NetworkHooks.openScreen((ServerPlayer) player, getGuiProviderFromSideTab(tabIndex, simulate), (buffer) -> buffer.writeInt(getId()));
        }
        return true;
    }

    public MenuProvider getGuiProviderFromSideTab(int tabIndex, boolean simulate) {
        if (tabIndex == 0 && task instanceof ILittleMaidTask littleMaidTask) {
            return littleMaidTask.getGuiProvider((EntityMaid) (Object) this, getId(), simulate);
        }
        switch (tabIndex) {
            case 0:
//                return this.getMaidBackpackType().getGuiProvider(getId());
//                return TaskConfigerContainer.create(getId());
            default:
                return this.getMaidBackpackType().getGuiProvider(getId());
        }
    }

    public CompoundTag getCookTaskTag() {
        CompoundTag addonMaidData$tlma = getAddonMaidData$tlma();
        return addonMaidData$tlma.getCompound("CookTask")
                .getCompound(getTask().getUid().getPath());
    }

    public ListTag getRecipeListTag() {
        return getCookTaskTag().getList(RECIPE, Tag.TAG_STRING);
    }

    public CookTaskData getCookTaskData1() {
        return cookTaskData;
    }

    @Override
    public void addOrRemoveRecipe1(String recipeId) {
        CookTaskData cookTaskData = this.getCookTaskData1();
        cookTaskData.addOrRemoveTaskRecipe(getTask().getUid().toString(), recipeId);
    }

    @Override
    public boolean containsRecipe1(String recipeId) {
        CookTaskData cookTaskData = this.getCookTaskData1();
        return cookTaskData.containsRecipe(getTask().getUid().toString(), recipeId);
    }

    public void toggleTaskRuleMode1() {
        CookTaskData cookTaskData = this.getCookTaskData1();
        cookTaskData.toggleMode(getTask().getUid().toString());
    }

    @Override
    public void addOrRemoveRecipe2(String recipeId) {
        CompoundTag persistentData = this.getPersistentData();
        CompoundTag compound = persistentData.getCompound(getTask().getUid().toString());
        if (compound.isEmpty()) {
            compound = new CompoundTag();
        }
        ListTag list = compound.getList(RECIPE, Tag.TAG_STRING);
        if (list.isEmpty()) {
            list = new ListTag();
        }
        if (list.contains(StringTag.valueOf(recipeId))) {
            list.remove(StringTag.valueOf(recipeId));
        } else {
            if (list.size() < TaskConfig.COOK_SELECTED_RECIPES.get()) {
                list.add(StringTag.valueOf(recipeId));
            }
        }
        compound.put(RECIPE, list);

        persistentData.put(getTask().getUid().toString(), compound);
    }

    @Override
    public boolean containsRecipe2(String recipeId) {
        CompoundTag persistentData = this.getPersistentData();
        CompoundTag compound = persistentData.getCompound(getTask().getUid().toString());
        ListTag list = compound.getList(RECIPE, Tag.TAG_STRING);
        return list.contains(StringTag.valueOf(recipeId));
    }
}
