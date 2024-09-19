package com.github.catbert.tlma.api;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;

import java.lang.ref.WeakReference;

 public interface IAddonMaid {

    WeakReference<FakePlayer> getFakePlayer$tlma();

    void initFakePlayer$tlma();

    Integer getStartYOffset$tlma();

    void setStartYOffset$tlma(int offset);

    boolean openMaidGuiFromSideTab(Player player, int tabIndex, boolean taskListOpen, int taskPage);

    MenuProvider getGuiProviderFromSideTab(int tabIndex, boolean taskListOpen, int taskPage);
}
