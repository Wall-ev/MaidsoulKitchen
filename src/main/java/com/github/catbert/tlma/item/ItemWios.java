package com.github.catbert.tlma.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityController;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class ItemWios extends ItemWirelessIO {

    private static final int FILTER_LIST_SIZE = 9;
    private static final String FILTER_LIST_TAG = "ItemFilterList";
    private static final String FILTER_MODE_TAG = "ItemFilterMode";
    private static final String IO_MODE_TAG = "ItemIOMode";
    private static final String BINDING_POS = "BindingPos";
    private static final String SLOT_CONFIG_TAG = "SlotConfigData";
    private static final String TOOLTIPS_PREFIX = "§a▍ §7";
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
        if (player == null) {
            return super.useOn(context);
        }

        if (te instanceof BlockEntityController) {
            ItemStack stack = player.getMainHandItem();
            setBindingPos(stack, pos);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (handIn == InteractionHand.MAIN_HAND && playerIn instanceof ServerPlayer) {
            NetworkHooks.openScreen((ServerPlayer) playerIn, this, (buffer) -> buffer.writeItem(playerIn.getMainHandItem()));
            return InteractionResultHolder.success(playerIn.getMainHandItem());
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new WirelessIOContainer(id, inventory, player.getMainHandItem());
    }

    public static void setBindingPos(ItemStack stack, BlockPos pos) {
        if (stack.getItem() instanceof ItemWirelessIO) {
            stack.getOrCreateTag().put("BindingPos", NbtUtils.writeBlockPos(pos));
        }
    }

    @Nullable
    public static BlockPos getBindingPos(ItemStack stack) {
        if (stack.getItem() instanceof ItemWirelessIO) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains(BINDING_POS, Tag.TAG_COMPOUND)) {
                return NbtUtils.readBlockPos(tag.getCompound(BINDING_POS));
            }
        }
        return null;
    }
}
