package com.github.wallev.maidsoulkitchen.compat.msm.baker;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.satisfy.bakery.core.registry.TabRegistry;

public class BakeryLang {

    @ModLang(Mods.DBK)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return TabRegistry.BAKERY_TAB.get().getDisplayName();
    };

}
