package com.github.wallev.maidsoulkitchen.compat.msm.farmersdelight;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import vectorwing.farmersdelight.common.registry.ModCreativeTabs;

public class Lang {

    @ModLang(Mods.FD)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return ModCreativeTabs.TAB_FARMERS_DELIGHT.get().getDisplayName();
    };

}
