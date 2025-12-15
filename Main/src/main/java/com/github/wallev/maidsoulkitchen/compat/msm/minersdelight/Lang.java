package com.github.wallev.maidsoulkitchen.compat.msm.minersdelight;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.sammy.minersdelight.setup.MDCreativeTabs;

public class Lang {

    @ModLang(Mods.MD)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return MDCreativeTabs.TAB_MINERS_DELIGHT.get().getDisplayName();
    };

}
