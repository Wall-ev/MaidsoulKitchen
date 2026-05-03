package com.github.wallev.maidsoulkitchen.compat.msm.immortalers_delight;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightGroup;

public class Lang {

    @ModLang(Mods.IMD)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return ImmortalersDelightGroup.TAB_FARMERS_DELIGHT.get().getDisplayName();
    };

}
