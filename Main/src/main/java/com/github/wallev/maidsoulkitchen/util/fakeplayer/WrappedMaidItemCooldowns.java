package com.github.wallev.maidsoulkitchen.util.fakeplayer;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ServerItemCooldowns;

public class WrappedMaidItemCooldowns extends ServerItemCooldowns {
    public WrappedMaidItemCooldowns(WrappedMaidFakePlayer maidFakePlayer) {
        super(maidFakePlayer);
    }
    @Override
    protected void onCooldownStarted(Item pItem, int pTicks) {
//        super.onCooldownStarted(pItem, pTicks);
    }

    @Override
    protected void onCooldownEnded(Item pItem) {
//        super.onCooldownEnded(pItem);
    }

    @Override
    public boolean isOnCooldown(Item pItem) {
        return false;
//        return super.isOnCooldown(pItem);
    }

    @Override
    public float getCooldownPercent(Item pItem, float pPartialTicks) {
        return 0f;
//        return super.getCooldownPercent(pItem, pPartialTicks);
    }

    @Override
    public void tick() {
//        super.tick();
    }

    @Override
    public void addCooldown(Item pItem, int pTicks) {
//        super.addCooldown(pItem, pTicks);
    }

    @Override
    public void removeCooldown(Item pItem) {
//        super.removeCooldown(pItem);
    }
}
