package com.catbert.tlma.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.FakePlayer;

import java.lang.ref.WeakReference;

public interface IAddonMaid {

    WeakReference<FakePlayer> getFakePlayer$tlma();

    void initFakePlayer$tlma();

    CompoundTag getAddonMaidData();

    void setAddonMaidData(CompoundTag nbt);

    void setStartYOffset$tlma(int offset);
    Integer getStartYOffset$tlma();

}
