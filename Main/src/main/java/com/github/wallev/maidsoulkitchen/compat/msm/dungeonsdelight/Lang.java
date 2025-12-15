package com.github.wallev.maidsoulkitchen.compat.msm.dungeonsdelight;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.yirmiri.dungeonsdelight.core.registry.DDCreativeTabs;

public class Lang {

    @ModLang(Mods.DUNGEONS_DELIGHT)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return DDCreativeTabs.DUNGEONSDELIGHT.get().getDisplayName();
    };

}
