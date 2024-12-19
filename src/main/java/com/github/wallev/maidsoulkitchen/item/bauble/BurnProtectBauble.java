package com.github.wallev.maidsoulkitchen.item.bauble;

import com.github.wallev.maidsoulkitchen.api.ILittleMaidBauble;
import com.github.wallev.maidsoulkitchen.datagen.ModDamageTypeTags;
import com.github.wallev.maidsoulkitchen.init.InitEffects;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAttackEvent;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDamageEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class BurnProtectBauble implements ILittleMaidBauble {

    public BurnProtectBauble() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDamage(MaidDamageEvent event) {
        EntityMaid maid = event.getMaid();
        DamageSource source = event.getSource();
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
        if (maid.hasEffect(InitEffects.BURN_PROTECT.get()) && source.is(ModDamageTypeTags.DAMAGES_BURN)) {
            event.setCanceled(true);
        }
    }
}
