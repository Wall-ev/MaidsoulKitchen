package com.github.wallev.maidsoulkitchen.compat.msm.simplefarming;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import dev.enemeez.simplefarming.common.registries.ModCreativeTabs;

public class Lang {

    @ModLang(Mods.SF)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return ModCreativeTabs.TAB.get().getDisplayName();
    };

}
