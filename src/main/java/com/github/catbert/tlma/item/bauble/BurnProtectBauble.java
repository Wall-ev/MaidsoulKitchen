package com.github.catbert.tlma.item.bauble;

import com.github.catbert.tlma.api.ILittleMaidBauble;
import com.github.catbert.tlma.data.ModDamageTypeTags;
import com.github.catbert.tlma.init.InitEffects;
import com.github.catbert.tlma.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAttackEvent;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDamageEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.github.catbert.tlma.item.bauble.BurnDamageManager.getBurnDamageTypes;

public class BurnProtectBauble implements ILittleMaidBauble {

    public BurnProtectBauble() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDamage(MaidDamageEvent event) {
        EntityMaid maid = event.getMaid();
        DamageSource source = event.getSource();
        List<ResourceKey<DamageType>> fireDamageTypes = getBurnDamageTypes();
        if (source.is(ModDamageTypeTags.DAMAGES_BURN)) {
            int slot = ItemsUtil.getBaubleSlotInMaid(maid, this);
            if (slot >= 0) {
                event.setCanceled(true);
                ItemStack stack = maid.getMaidBauble().getStackInSlot(slot);
                stack.hurtAndBreak(1, maid, m -> maid.sendItemBreakMessage(stack));
                maid.getMaidBauble().setStackInSlot(slot, stack);
                maid.addEffect(new MobEffectInstance(InitEffects.BURN_PROTECT.get(), 300));
                if (!maid.level.isClientSide) {
                    maid.level.addFreshEntity(new EntityExtinguishingAgent(maid.level(), maid.position()));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBurnDamage(MaidAttackEvent event) {
        EntityMaid maid = event.getMaid();
        DamageSource source = event.getSource();
        List<ResourceKey<DamageType>> burnDamageTypes = getBurnDamageTypes();
        if (maid.hasEffect(InitEffects.BURN_PROTECT.get()) && source.is(ModDamageTypeTags.DAMAGES_BURN)) {
            event.setCanceled(true);
        }
    }
}
