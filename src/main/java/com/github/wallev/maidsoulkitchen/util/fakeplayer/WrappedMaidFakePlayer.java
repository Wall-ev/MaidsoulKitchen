package com.github.wallev.maidsoulkitchen.util.fakeplayer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@ApiStatus.Experimental
public class WrappedMaidFakePlayer extends FakePlayer {

    private static final ConcurrentHashMap<UUID, WrappedMaidFakePlayer> CACHE = new ConcurrentHashMap<>();
    private final EntityMaid maid;
    private ItemStack ghostItem = ItemStack.EMPTY;
    private boolean enableGhostItem = false;
    private boolean needSync = false;

    private WrappedMaidFakePlayer(EntityMaid maid) {
        super((ServerLevel) maid.level(), new GameProfile(UUID.randomUUID(), maid.getName().getString()));
        this.maid = maid;
        this.inventory = new WrappedMaidInventory(maid, this);

        this.gameMode = new ServerPlayerGameMode(this) {
            @Override
            public boolean changeGameModeForPlayer(GameType pGameModeForPlayer) {
                return false;
            }
        };
    }

    public static WrappedMaidFakePlayer get(EntityMaid maid) {
        if (CACHE.containsKey(maid.getUUID())) {
            WrappedMaidFakePlayer wrappedMaidFakePlayer = CACHE.get(maid.getUUID());
            if (!wrappedMaidFakePlayer.maid.isAlive()) {
                CACHE.remove(maid.getUUID());
            } else {
                return wrappedMaidFakePlayer;
            }
        }
        WrappedMaidFakePlayer fakePlayer = new WrappedMaidFakePlayer(maid);
        CACHE.put(maid.getUUID(), fakePlayer);
        return fakePlayer;
    }

    @Override
    public WrappedMaidInventory getInventory() {
        return (WrappedMaidInventory) super.getInventory();
    }

    @Override
    public boolean addItem(ItemStack stack) {
        return this.getInventory().add(stack);
    }

    @Override
    public boolean drop(boolean dropStack) {
        return super.drop(dropStack);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
    }

    @Override
    protected void destroyVanishingCursedItems() {
        super.destroyVanishingCursedItems();
    }

    @Override
    protected void hurtArmor(DamageSource damageSource, float damage) {
        super.hurtArmor(damageSource, damage);
    }

    @Override
    protected void hurtHelmet(DamageSource damageSource, float damageAmount) {
        super.hurtHelmet(damageSource, damageAmount);
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack Stack) {
        super.setItemSlot(slot, Stack);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return super.getArmorSlots();
    }

    @Override
    public SlotAccess getSlot(int slot) {
        return super.getSlot(slot);
    }

    @Override
    public ItemStack getProjectile(ItemStack shoot) {
        return super.getProjectile(shoot);
    }

    @Override
    public ItemStack getMainHandItem() {
        return super.getMainHandItem();
    }

    @Override
    public ItemStack getOffhandItem() {
        return super.getOffhandItem();
    }

    @Override
    public ItemStack getItemInHand(InteractionHand pHand) {
        return enableGhostItem ? ghostItem : super.getItemInHand(pHand);
    }

    @Override
    public void setItemInHand(InteractionHand pHand, ItemStack pStack) {
        if (maid == null) {
            return;
        }

        // @todo：还待更多的测试
        if (enableGhostItem) {
            if (ItemStack.isSameItemSameTags(this.ghostItem, pStack)) {
                this.ghostItem.shrink(this.ghostItem.getCount() - pStack.getCount());
            } else {
                this.ghostItem.shrink(1);
                this.addItem(pStack);
            }

        } else {
            maid.setItemInHand(pHand, pStack);
        }
    }

    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        if (maid == null) {
            return ItemStack.EMPTY;
        }

        if (enableGhostItem) {
            return ghostItem;
        }

        switch (equipmentSlot) {
            case MAINHAND -> {
                return maid.getMainHandItem();
            }
            case OFFHAND -> {
                return maid.getOffhandItem();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isHolding(Item pItem) {
        return super.isHolding(pItem);
    }

    @Override
    public boolean isHolding(Predicate<ItemStack> pPredicate) {
        return super.isHolding(pPredicate);
    }

    public void swing(InteractionHand hand) {
        this.swing(hand, false);
    }

    public void swing(InteractionHand hand, boolean updateSelf) {
        maid.swing(hand, updateSelf);
    }

    public void setGhostItem(ItemStack itemStack) {
        this.enableGhostItem = true;
        this.ghostItem = itemStack;
    }

    public void clearGhostItem() {
        this.enableGhostItem = false;
        this.ghostItem = ItemStack.EMPTY;
    }

    public void resetInv() {
        this.getInventory().resetInv();
    }

    public IItemHandlerModifiable getInv() {
        return this.getInventory().getInv();
    }

    public void setInv(IItemHandlerModifiable inv) {
        this.getInventory().setInvSupplier(maid -> inv);
    }

    public IItemHandlerModifiable getCombinedHandInv() {
        return new CombinedInvWrapper(maid.getHandsInvWrapper(), this.getInv());
    }


    public InteractionResult clickPos(BlockPos targetPos, ItemStack itemStack) {
        return this.useOnByItem(targetPos, itemStack);
    }

    public InteractionResult clickPos(BlockPos targetPos) {
        return this.useOnByHand(targetPos);
    }

    public InteractionResult clickPos() {
        return this.useOnByHand();
    }


    public InteractionResult use() {
        return this.useByHand(InteractionHand.MAIN_HAND);
    }

    public InteractionResult useByHand(InteractionHand hand) {
        return this.useByHand(hand, this.getItemInHand(hand));
    }

    public InteractionResult useByHand(InteractionHand hand, BlockPos pos) {
        return this.useByHand(hand, this.getItemInHand(hand), pos);
    }

    public InteractionResult useByHand(InteractionHand hand, ItemStack itemStack) {
        InteractionResult result = this.gameMode.useItem(this, maid.level, itemStack, hand);
        if (result.shouldSwing()) {
            this.swing(hand, true);
        }
        return result;
    }

    public InteractionResult useByHand(InteractionHand hand, ItemStack itemStack, BlockPos pos) {
        BlockHitResult blockHitResult = this.getBlockHitResult(pos);
        InteractionResult result = this.gameMode.useItemOn(this, maid.level, itemStack, hand, blockHitResult);
        if (result.shouldSwing()) {
            this.swing(hand, true);
        }
        return result;
    }

    public InteractionResult useOnByItem(BlockPos pos, ItemStack itemStack, IItemHandlerModifiable inv) {
        try {
            BlockHitResult blockHitResult = this.getBlockHitResult(pos);
            this.setGhostItem(itemStack);
            this.getInventory().setInvSupplier((maid0 -> inv));
            InteractionResult result = this.gameMode.useItemOn(this, maid.level, itemStack, InteractionHand.MAIN_HAND, blockHitResult);
            if (result.shouldSwing()) {
                this.swing(InteractionHand.MAIN_HAND, true);
            }
            this.clearGhostItem();
            this.getInventory().resetInv();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return InteractionResult.FAIL;
        }
    }

    public InteractionResult useOnByItem(BlockPos pos, ItemStack itemStack) {
        try {
            BlockHitResult blockHitResult = this.getBlockHitResult(pos);
            this.setGhostItem(itemStack);
            InteractionResult result = this.gameMode.useItemOn(this, maid.level, itemStack, InteractionHand.MAIN_HAND, blockHitResult);
            if (result.shouldSwing()) {
                this.swing(InteractionHand.MAIN_HAND, true);
            }
            this.clearGhostItem();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return InteractionResult.FAIL;
        }
    }

    public InteractionResult useOnByItem(BlockPos pos, ItemStack itemStack, boolean sneak) {
        try {
            if (sneak) {
                this.setShiftKeyDown(true);
            }
            BlockHitResult blockHitResult = this.getBlockHitResult(pos);
            this.setGhostItem(itemStack);
            InteractionResult result = this.gameMode.useItemOn(this, maid.level, itemStack, InteractionHand.MAIN_HAND, blockHitResult);
            if (result.shouldSwing()) {
                this.swing(InteractionHand.MAIN_HAND, true);
            }
            this.clearGhostItem();
            if (sneak) {
                this.setShiftKeyDown(false);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return InteractionResult.FAIL;
        }
    }

    public InteractionResult useOnByHand(BlockPos pos) {
        try {
            BlockHitResult blockHitResult = this.getBlockHitResult(pos);
            InteractionResult result = this.gameMode.useItemOn(this, maid.level, this.getMainHandItem(), InteractionHand.MAIN_HAND, blockHitResult);
            if (result.shouldSwing()) {
                this.swing(InteractionHand.MAIN_HAND, true);
            }
            return result;
//            return this.gameMode.useItem(this, maid.level, this.getMainHandItem(), InteractionHand.MAIN_HAND, blockHitResult);
        } catch (Exception e) {
            e.printStackTrace();
            return InteractionResult.FAIL;
        }
    }

    public InteractionResult useOnByHand() {
        try {
            return maid.getBrain().getMemory(MemoryModuleType.LOOK_TARGET)
                    .map(positionTracker -> {
                        InteractionResult result = this.useOnByHand(positionTracker.currentBlockPosition());
                        if (result.shouldSwing()) {
                            this.swing(InteractionHand.MAIN_HAND, true);
                        }
                        return result;
                    })
                    .orElse(InteractionResult.FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            return InteractionResult.FAIL;
        }
    }

    protected BlockHitResult getBlockHitResult() {
        return (BlockHitResult) maid.pick(7, 0, false);
    }

    @ApiStatus.Experimental
    protected BlockHitResult getBlockHitResultBy(BlockPos pos) {
        final float partialTicks = 0f;
        final double hitDistance = 6;
        final boolean hitFluids = false;

//        RayTraceUtil

        maid.getLookControl().setLookAt(pos.getCenter());
//        pos.equals(((BlockHitResult) maid.pick(hitDistance, partialTicks, hitFluids)).getBlockPos())
//        return (BlockHitResult) maid.pick(hitDistance, partialTicks, hitFluids);
        Vec3 eyePosition = maid.getEyePosition(partialTicks);
        Vec3 vec31 = maid.getViewVector(partialTicks);
        Vec3 vec32 = eyePosition.add(vec31.x * hitDistance, vec31.y * hitDistance, vec31.z * hitDistance);
//        pos.equals(maid.level.clip(new ClipContext(eyePosition, vec32, ClipContext.Block.OUTLINE, hitFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, this)).getBlockPos())
        return maid.level.clip(new ClipContext(eyePosition, vec32, ClipContext.Block.OUTLINE, hitFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, this));
    }

    // @todo
    // 射线有问题
    @ApiStatus.Experimental
    protected BlockHitResult getBlockHitResult(BlockPos pos) {
//        this.getMainHandItem().useOn()
        return new BlockHitResult(maid.getLookAngle(), maid.getMotionDirection(), pos, false);
//        return getLivingEntityPOVHitResult(pos, ClipContext.Fluid.NONE);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (maid != null && maid.isAlive() && capability == ForgeCapabilities.ITEM_HANDLER) {
            return LazyOptional.of(() -> (T) this.getInv());
        }

        return super.getCapability(capability, facing);
    }

    @ApiStatus.Experimental
    public BlockHitResult getLivingEntityPOVHitResult(BlockPos pos, ClipContext.Fluid fluidMode) {
        maid.getLookControl().setLookAt(pos.getCenter());

        float xRot = maid.getXRot();
        float yRot = maid.getYRot();
        Vec3 eyePosition = maid.getEyePosition();
        float f2 = Mth.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-xRot * ((float) Math.PI / 180F));
        float yo = Mth.sin(-xRot * ((float) Math.PI / 180F));
        float xo = f3 * f4;
        float zo = f2 * f4;
        double blockReach = this.getBlockReach();
        Vec3 vec31 = eyePosition.add(xo * blockReach, yo * blockReach, zo * blockReach);
        return level.clip(new ClipContext(eyePosition, vec31, ClipContext.Block.OUTLINE, fluidMode, maid));
    }

    public double getBlockReach() {
        AttributeInstance reachInstance = maid.getAttribute(ForgeMod.BLOCK_REACH.get());
        if (reachInstance != null) {
            return maid.getAttributeValue(ForgeMod.BLOCK_REACH.get());
        } else {
            return 6.0d;
        }
    }

    @Deprecated
    public double getFluidHeight(TagKey<Fluid> fluidTagKey) {
        if (maid == null) {
            return super.getFluidHeight(fluidTagKey);
        }

        return maid.getFluidHeight(fluidTagKey);
    }

    public double getFluidJumpThreshold() {
        if (maid == null) {
            return super.getFluidJumpThreshold();
        }

        return maid.getFluidJumpThreshold();
    }

    public final float getBbWidth() {
        if (maid == null) {
            return super.getBbWidth();
        }

        return maid.getBbWidth();
    }

    public final float getBbHeight() {
        if (maid == null) {
            return super.getBbHeight();
        }

        return maid.getBbHeight();
    }

    public float getNameTagOffsetY() {
        if (maid == null) {
            return super.getNameTagOffsetY();
        }

        return maid.getNameTagOffsetY();
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        if (maid == null) {
            return super.getAddEntityPacket();
        }

        return maid.getAddEntityPacket();
    }

    public EntityDimensions getDimensions(Pose pose) {
        if (maid == null) {
            return super.getDimensions(pose);
        }

        return maid.getDimensions(pose);
    }

    public Vec3 position() {
        if (maid == null) {
            return super.position();
        }

        return maid.position();
    }

    public Vec3 trackingPosition() {
        if (maid == null) {
            return super.trackingPosition();
        }

        return maid.trackingPosition();
    }

    public BlockPos blockPosition() {
        if (maid == null) {
            return super.blockPosition();
        }

        return maid.blockPosition();
    }

    public BlockState getFeetBlockState() {
        if (maid == null) {
            return super.getFeetBlockState();
        }

        return maid.getFeetBlockState();
    }

    public ChunkPos chunkPosition() {
        if (maid == null) {
            return super.chunkPosition();
        }

        return maid.chunkPosition();
    }

    public Vec3 getDeltaMovement() {
        if (maid == null) {
            return super.getDeltaMovement();
        }

        return maid.getDeltaMovement();
    }

    public void setDeltaMovement(Vec3 deltaMovement) {
        if (maid == null) {
            return;
        }

        maid.setDeltaMovement(deltaMovement);
    }

    public void addDeltaMovement(Vec3 add) {
        if (maid == null) {
            return;
        }

        maid.setDeltaMovement(add);
    }

    public void setDeltaMovement(double x, double y, double z) {
        if (maid == null) {
            return;
        }

        maid.setDeltaMovement(x, y, z);
    }

    public final int getBlockX() {
        if (maid == null) {
            return super.getBlockX();
        }

        return maid.getBlockX();
    }

    public final double getX() {
        if (maid == null) {
            return super.getX();
        }

        return maid.getX();
    }

    public double getX(double scale) {
        if (maid == null) {
            return super.getX(scale);
        }

        return maid.getX(scale);
    }

    public double getRandomX(double scale) {
        if (maid == null) {
            return super.getRandomX(scale);
        }

        return maid.getRandomX(scale);
    }

    public final int getBlockY() {
        if (maid == null) {
            return super.getBlockY();
        }

        return maid.getBlockY();
    }

    public final double getY() {
        if (maid == null) {
            return super.getY();
        }

        return maid.getY();
    }

    public double getY(double scale) {
        if (maid == null) {
            return super.getY(scale);
        }

        return maid.getY(scale);
    }

    public double getRandomY() {
        if (maid == null) {
            return super.getRandomY();
        }

        return maid.getRandomY();
    }

    public double getEyeY() {
        if (maid == null) {
            return super.getEyeY();
        }

        return maid.getEyeY();
    }

    public final int getBlockZ() {
        if (maid == null) {
            return super.getBlockZ();
        }

        return maid.getBlockZ();
    }

    public final double getZ() {
        if (maid == null) {
            return super.getZ();
        }

        return maid.getZ();
    }

    public double getZ(double scale) {
        if (maid == null) {
            return super.getZ(scale);
        }

        return maid.getZ(scale);
    }

    public double getRandomZ(double scale) {
        if (maid == null) {
            return super.getRandomZ(scale);
        }

        return maid.getRandomZ(scale);
    }

    public float getYRot() {
        if (maid == null) {
            return super.getYRot();
        }

        return maid.getYRot();
    }

    public void setYRot(float pYRot) {
        if (maid == null) {
            super.setYRot(pYRot);
            return;
        }

        maid.setYRot(pYRot);
    }

    public float getVisualRotationYInDegrees() {
        if (maid == null) {
            return super.getVisualRotationYInDegrees();
        }

        return maid.getVisualRotationYInDegrees();
    }

    public float getXRot() {
        if (maid == null) {
            return super.getXRot();
        }

        return maid.getXRot();
    }

    public void setXRot(float pXRot) {
        if (maid == null) {
            super.setXRot(pXRot);
            return;
        }

        maid.setXRot(pXRot);
    }


//    public boolean isNeedSync() {
//        return needSync;
//    }
//
//    public void setNeedSync(boolean needSync) {
//        this.needSync = needSync;
//    }
//
//    public void sync() {
//        this.position = maid.position();
//        this.blockPosition = maid.blockPosition();
//        this.eyeHeight = maid.getEyeHeight();
//        this.dimensions = maid.dimensions;
//        this.deltaMovement = maid.getDeltaMovement();
//    }
}