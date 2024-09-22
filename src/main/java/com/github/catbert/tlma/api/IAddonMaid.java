package com.github.catbert.tlma.api;

import net.minecraftforge.common.util.FakePlayer;

import java.lang.ref.WeakReference;

public interface IAddonMaid {

    WeakReference<FakePlayer> getFakePlayer$tlma();

    void initFakePlayer$tlma();

}
