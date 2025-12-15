package com.github.wallev.maidsoulkitchen.item.bauble;

import com.github.wallev.maidsoulkitchen.api.bauble.IMaidsoulKitchenBauble;
import com.github.wallev.maidsoulkitchen.datagen.ModDamageTypeTags;
import com.github.wallev.maidsoulkitchen.init.ModEffects;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAttackEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.mutable.MutableFloat;


public class BurnProtectBauble implements IMaidsoulKitchenBauble {

    public BurnProtectBauble() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean onInjured(EntityMaid maid, ItemStack baubleItem, DamageSource source, MutableFloat damage) {
        if (source.is(ModDamageTypeTags.DAMAGES_BURN)) {
            baubleItem.hurtAndBreak(1, maid, m -> maid.sendItemBreakMessage(baubleItem));
            maid.addEffect(new MobEffectInstance(ModEffects.BURN_PROTECT.get(), 300));
            if (!maid.level.isClientSide) {
                maid.level.addFreshEntity(new EntityExtinguishingAgent(maid.level, maid.position()));
            }
            return true;
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBurnDamage(MaidAttackEvent event) {
        EntityMaid maid = event.getMaid();
        DamageSource source = event.getSource();
        if (maid.hasEffect(ModEffects.BURN_PROTECT.get()) && source.is(ModDamageTypeTags.DAMAGES_BURN)) {
            event.setCanceled(true);
        }
    }
}
