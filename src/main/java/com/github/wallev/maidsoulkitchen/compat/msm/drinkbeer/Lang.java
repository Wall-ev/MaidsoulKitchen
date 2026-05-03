package com.github.wallev.maidsoulkitchen.compat.msm.drinkbeer;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import lekavar.lma.drinkbeer.registries.CreativeTabRegistry;

public class Lang {

    @ModLang(
            value = Mods.DB,
            en_us = "Drink Beer",
            zh_cn = "喝啤酒啦",
            custom = true
    )
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return CreativeTabRegistry.GENERAL.get().getDisplayName();
    };

}
