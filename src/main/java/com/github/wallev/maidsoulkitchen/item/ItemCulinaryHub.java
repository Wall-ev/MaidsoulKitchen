package com.github.wallev.maidsoulkitchen.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.TabIndex;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.config.MaidConfigContainer;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.init.MkItems;
import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.inventory.container.item.CookBagAbstractContainer;
import com.github.wallev.maidsoulkitchen.inventory.container.item.CookBagConfigContainer;
import com.github.wallev.maidsoulkitchen.inventory.container.item.CookBagContainer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.STORAGE_DATA_TAG;
import static net.minecraft.core.registries.Registries.DATA_COMPONENT_TYPE;

public class ItemCulinaryHub extends Item implements MenuProvider {
    public static final int INV_SLOT = 4;
    private static final int COOK_BAG_SIZE = getCookBagSize();
    private static final String CONTAINER_TAG = "container";
    private static final String BIND_MODE_TAG = "binding_mode";
    private static final String BIND_POS_TAG = "binding_pos";
    public static final String STORAGE_DATA_TAG_NAME = "storage_data";
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(DATA_COMPONENT_TYPE, MaidsoulKitchen.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> STORAGE_DATA_TAG = DATA_COMPONENTS
            .register(STORAGE_DATA_TAG_NAME, () -> DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<BlockPos>>> BINDING_POSES = DATA_COMPONENTS
            .register(BIND_POS_TAG, () -> DataComponentType.<List<BlockPos>>builder().persistent(Codec.list(BlockPos.CODEC))
                    .networkSynchronized(ByteBufCodecs.fromCodec(Codec.list(BlockPos.CODEC)))
                    .build());

    public static final int BIND_SIZE = 3;

    public ItemCulinaryHub() {
        super(new Item.Properties().stacksTo(1));
    }

    public static void removeModePoses(ItemStack stack) {
        if (stack.is(MkItems.CULINARY_HUB.get())) {
            CompoundTag tag = stack.getOrDefault(STORAGE_DATA_TAG, new CompoundTag());
            CompoundTag compound = tag.getCompound(BIND_POS_TAG);

            for (BagType value : BagType.values()) {
                compound.remove(value.name);
            }

            tag.put(BIND_POS_TAG, compound);
        }
    }

    public static void actionModePos(ItemStack stack, String mode, BlockPos blockPos) {
        if (stack.is(MkItems.CULINARY_HUB.get()) && !mode.isEmpty()) {
            CompoundTag tag = stack.getOrDefault(STORAGE_DATA_TAG, new CompoundTag());
            CompoundTag compound = tag.getCompound(BIND_POS_TAG);
            ListTag list = compound.getList(mode, Tag.TAG_INT_ARRAY);
            AtomicBoolean remove = new AtomicBoolean(false);

            for (Tag tag1 : list) {
                BlockPos pos = readBlockPos((IntArrayTag) tag1);
                if (pos.equals(blockPos)) {
                    remove.set(true);
                    list.remove(tag1);
                    break;
                }
            }

            if (!remove.get()) {
                list.add(NbtUtils.writeBlockPos(blockPos));
                compound.put(mode, list);
            }
            tag.put(BIND_POS_TAG, compound);
            stack.set(STORAGE_DATA_TAG, tag);
        }
    }

    public static List<BlockPos> getBindModePoses(ItemStack stack, String mode) {
        if (stack.is(MkItems.CULINARY_HUB.get())) {
            CompoundTag tag = stack.get(STORAGE_DATA_TAG);
            if (tag != null && tag.contains(BIND_POS_TAG, Tag.TAG_COMPOUND)) {
                CompoundTag tag1 = tag.getCompound(BIND_POS_TAG);
                ListTag list = tag1.getList(mode, Tag.TAG_INT_ARRAY);
                return list.stream().map(tag2 -> readBlockPos((IntArrayTag) tag2)).toList();
            }
        }
        return Collections.emptyList();
    }

    public static BlockPos readBlockPos(IntArrayTag arrayTag) {
        int[] asIntArray = arrayTag.getAsIntArray();
        return new BlockPos(asIntArray[0], asIntArray[1], asIntArray[2]);
    }

    public static Map<BagType, List<BlockPos>> getBindPoses(ItemStack stack) {
        if (stack.is(MkItems.CULINARY_HUB.get())) {
            CompoundTag tag = stack.getOrDefault(STORAGE_DATA_TAG, new CompoundTag());
            if (tag.contains(BIND_POS_TAG, Tag.TAG_COMPOUND)) {
                CompoundTag tag1 = tag.getCompound(BIND_POS_TAG);

                HashMap<BagType, List<BlockPos>> typeListHashMap = new HashMap<>();
                for (BagType value : BagType.values()) {
                    ListTag list = tag1.getList(value.name, Tag.TAG_INT_ARRAY);
                    List<BlockPos> poses = list.stream().map(tag2 -> readBlockPos((IntArrayTag)tag2)).toList();
                    typeListHashMap.put(value, poses);
                }

                return typeListHashMap;
            }
        }
        return Map.of();
    }

    public static String getBindMode(ItemStack stack) {
        if (stack.is(MkItems.CULINARY_HUB.get())) {
            CompoundTag tag = stack.get(STORAGE_DATA_TAG);
            if (tag != null) {
                return tag.getString(BIND_MODE_TAG);
            }
        }
        return "";
    }

    public static void setBindModeTag(ItemStack stack, String mode) {
        if (stack.is(MkItems.CULINARY_HUB.get())) {
            CompoundTag tag = stack.getOrDefault(STORAGE_DATA_TAG, new CompoundTag());
            tag.putString(BIND_MODE_TAG, mode);
            stack.set(STORAGE_DATA_TAG, tag);
        }
    }

    private static int getCookBagSize() {
        int size = 0;
        for (BagType value : BagType.values()) {
            size += value.size * 9;
        }
        return size;
    }

    public static Map<BagType, ItemStackHandler> getContainers(HolderLookup.Provider provider, ItemStack stack) {
        Map<BagType, ItemStackHandler> bagTypeItemStackHandlerHashMap = new HashMap<>();
        if (stack.is(MkItems.CULINARY_HUB.get())) {
            CompoundTag tag = stack.getOrDefault(STORAGE_DATA_TAG, new CompoundTag());
            if (tag == null || !tag.contains(CONTAINER_TAG, Tag.TAG_COMPOUND)) {
                for (BagType value : BagType.values()) {
                    ItemStackHandler handler = new ItemStackHandler(value.size * 9);
                    bagTypeItemStackHandlerHashMap.put(value, handler);
                }
            } else {
                CompoundTag compound = tag.getCompound(CONTAINER_TAG);
                for (BagType value : BagType.values()) {
                    ItemStackHandler handler = new ItemStackHandler(value.size * 9);
                    if (compound.contains(value.name, Tag.TAG_COMPOUND)) {
                        handler.deserializeNBT(provider, compound.getCompound(value.name));
                    }
                    bagTypeItemStackHandlerHashMap.put(value, handler);
                }
            }
        }
        return bagTypeItemStackHandlerHashMap;
    }

    public static void setContainer(HolderLookup.Provider provider, ItemStack stack, Map<BagType, ItemStackHandler> handlers) {
        if (stack.is(MkItems.CULINARY_HUB.get())) {
            CompoundTag tag = stack.getOrDefault(STORAGE_DATA_TAG, new CompoundTag());
            CompoundTag compound = tag.getCompound(CONTAINER_TAG);
            handlers.forEach((bagType, itemStackHandler) -> {
                compound.put(bagType.name, itemStackHandler.serializeNBT(provider));
            });
            tag.put(CONTAINER_TAG, compound);
            stack.set(STORAGE_DATA_TAG, tag);
        }
    }

    public static ItemStackHandler getContainer(HolderLookup.Provider provider, ItemStack stack) {
        ItemStackHandler handler = new ItemStackHandler(COOK_BAG_SIZE);
        if (stack.is(MkItems.CULINARY_HUB.get())) {
            CompoundTag tag = stack.getOrDefault(STORAGE_DATA_TAG, new CompoundTag());
            if (tag != null && tag.contains(CONTAINER_TAG, Tag.TAG_COMPOUND)) {
                handler.deserializeNBT(provider, tag.getCompound(CONTAINER_TAG));
            }
        }
        return handler;
    }

    public static void setContainer(HolderLookup.Provider provider, ItemStack stack, ItemStackHandler itemStackHandler) {
        if (stack.is(MkItems.CULINARY_HUB.get())) {
            CompoundTag tag = stack.getOrDefault(STORAGE_DATA_TAG, new CompoundTag());
            tag.put(CONTAINER_TAG, itemStackHandler.serializeNBT(provider));
            stack.set(STORAGE_DATA_TAG, tag);
        }
    }

    public static boolean openCookBagGuiFromSideTab(Player player, int tabIndex) {
        if (player instanceof ServerPlayer) {
            player.openMenu(getGuiProviderFromSideTab(tabIndex), buffer -> ItemStack.STREAM_CODEC.encode(buffer, player.getMainHandItem()));
        }
        return true;
    }

    private static MenuProvider getGuiProviderFromSideTab(int tabIndex) {
        if (tabIndex == 0) {
            return getCookBagConfigContainer();
        } else {
            return getCookBagContainer();
        }
    }

    private static MenuProvider getCookBagContainer() {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Cook Container");
            }

            @Override
            public CookBagAbstractContainer createMenu(int index, Inventory playerInventory, Player player) {
                return new CookBagContainer(index, playerInventory, player.getMainHandItem());
            }

            @Override
            public boolean shouldTriggerClientSideContainerClosingOnOpen() {
                return false;
            }
        };
    }

    private static MenuProvider getCookBagConfigContainer() {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Cook Config Container");
            }

            @Override
            public CookBagAbstractContainer createMenu(int index, Inventory playerInventory, Player player) {
                return new CookBagConfigContainer(index, playerInventory, player.getMainHandItem());
            }

            @Override
            public boolean shouldTriggerClientSideContainerClosingOnOpen() {
                return false;
            }
        };
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        BlockEntity te = worldIn.getBlockEntity(pos);

        if (hand != InteractionHand.MAIN_HAND) {
            return super.useOn(context);
        }
        if (player == null || te == null) {
            return super.useOn(context);
        }

        // TLM
        for (IChestType allChestType : ChestManager.getAllChestTypes()) {
            if (allChestType.isChest(te) && allChestType.canOpenByPlayer(te, player)) {
                ItemStack stack = player.getMainHandItem();
                String bindMode = getBindMode(stack);
                List<BlockPos> bindModePoses = getBindModePoses(stack, bindMode);
                if (bindModePoses.size() >= BIND_SIZE && !bindModePoses.contains(pos)) {
                    if (context.getLevel().isClientSide) {
                        player.sendSystemMessage(Component.translatable("message.maidsoulkitchen.culinary_hub.bine_type_max"));
                    }
                    return InteractionResult.sidedSuccess(worldIn.isClientSide);
                }
                if (!bindMode.isEmpty()) {
                    actionModePos(stack, bindMode, pos);
                    return InteractionResult.sidedSuccess(worldIn.isClientSide);
                }
            }
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (handIn == InteractionHand.MAIN_HAND && playerIn instanceof ServerPlayer) {
            playerIn.openMenu(this, buffer -> ItemStack.STREAM_CODEC.encode(buffer, playerIn.getMainHandItem()));
            return InteractionResultHolder.success(playerIn.getMainHandItem());
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Cook Bag Container");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CookBagConfigContainer(pContainerId, pPlayerInventory, pPlayer.getMainHandItem());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.usage").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.usage.0").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.usage").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.usage.1").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.usage.2").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.usage.3").withStyle(ChatFormatting.GRAY));
        }


        {
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.function").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.function.1").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.function.2").withStyle(ChatFormatting.GRAY));
        }

        Map<BagType, List<BlockPos>> bindPoses = ItemCulinaryHub.getBindPoses(stack);
        List<BagType> leftBindBagTypes = new ArrayList<>();
        bindPoses.forEach((type, poses) -> {
            if (poses.isEmpty() && !(type == BagType.INGREDIENT_ADDITION || type == BagType.START_ADDITION)) {
                leftBindBagTypes.add(type);
            }
        });
        if (bindPoses.isEmpty() || leftBindBagTypes.size() == BagType.values().length - 2) {
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.warn").withStyle(ChatFormatting.YELLOW));
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.warn.empty").withStyle(ChatFormatting.GRAY));
        } else if (!leftBindBagTypes.isEmpty()) {
            MutableComponent leftComponent1 = Component.empty();
            boolean first = true;
            for (BagType value : leftBindBagTypes) {
                if (first) {
                    leftComponent1.append(Component.translatable("gui.maidsoulkitchen.culinary_hub.config.bind_mode." + value.translateKey).withStyle(ChatFormatting.GRAY));
                    first = false;
                } else {
                    leftComponent1.append(Component.literal("、").append(Component.translatable("gui.maidsoulkitchen.culinary_hub.config.bind_mode." + value.translateKey).withStyle(ChatFormatting.GRAY)));
                }
            }
            MutableComponent leftComponent = Component.literal("[").append(leftComponent1).append(Component.literal("]").withStyle(ChatFormatting.GRAY));

            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.warn").withStyle(ChatFormatting.YELLOW));
            tooltip.add(Component.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.warn.left", leftComponent).withStyle(ChatFormatting.GRAY));
        }
    }
}
