package com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.condition;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;

public record ModsCondition(Mods mods, boolean modMark) implements LoadCondition<Mods> {

    @Override
    public boolean canLoad() {
        return mods.versionLoad();
    }

    @Override
    public Mods condition() {
        return mods;
    }
}
