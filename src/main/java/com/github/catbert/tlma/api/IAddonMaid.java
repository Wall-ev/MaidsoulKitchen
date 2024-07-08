package com.github.catbert.tlma.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.FakePlayer;

import java.lang.ref.WeakReference;

public interface IAddonMaid {

    WeakReference<FakePlayer> getFakePlayer$tlma();

    void initFakePlayer$tlma();

    CompoundTag getAddonMaidData$tlma();

    void setAddonMaidData$tlma(CompoundTag nbt);

    void setStartYOffset$tlma(int offset);
    Integer getStartYOffset$tlma();

}
