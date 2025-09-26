package com.github.wallev.maidsoulkitchen.compat.msm.kaleidoscopecookery;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModCreativeTabs;

public class Lang {

    @ModLang(Mods.KC)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return ModCreativeTabs.COOKERY_MAIN_TAB.get().getDisplayName();
    };

}
