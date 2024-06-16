package com.catbert.tlma.api;

import net.minecraftforge.common.util.FakePlayer;

import java.lang.ref.WeakReference;

public interface IMaidAddon {

    WeakReference<FakePlayer> getFakePlayer$tlma();

    void initFakePlayer$tlma();

    void setStartYOffset$tlma(int offset);
    Integer getStartYOffset$tlma();

}
