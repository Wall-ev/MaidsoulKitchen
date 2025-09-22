package com.github.wallev.maidsoulkitchen.compat.msm.kitchenkarrot;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import io.github.tt432.kitchenkarrot.registries.ModTabs;
import lekavar.lma.drinkbeer.registries.CreativeTabRegistry;

public class Lang {

    @ModLang(Mods.KK)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return ModTabs.MAIN_TAB.get().getDisplayName();
    };

}
