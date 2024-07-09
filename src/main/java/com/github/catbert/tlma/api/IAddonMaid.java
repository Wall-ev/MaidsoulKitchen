package com.github.catbert.tlma.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;

import java.lang.ref.WeakReference;

public interface IAddonMaid {

    WeakReference<FakePlayer> getFakePlayer$tlma();

    void initFakePlayer$tlma();

    CompoundTag getAddonMaidData$tlma();

    void setAddonMaidData$tlma(CompoundTag nbt);

    Integer getStartYOffset$tlma();

    void setStartYOffset$tlma(int offset);

    boolean openMaidGuiFromSideTab(Player player, int tabIndex);

    MenuProvider getGuiProviderFromSideTab(int tabIndex);

}
