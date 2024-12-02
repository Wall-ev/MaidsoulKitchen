package com.github.wallev.farmsoulkitchen.entity.passive;

import net.minecraftforge.common.util.FakePlayer;

import java.lang.ref.WeakReference;

public interface IAddonMaid {

    WeakReference<FakePlayer> getFakePlayer$tlma();

    void initFakePlayer$tlma();

}
