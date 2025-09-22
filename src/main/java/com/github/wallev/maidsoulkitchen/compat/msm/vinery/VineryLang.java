package com.github.wallev.maidsoulkitchen.compat.msm.vinery;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.satisfy.vinery.core.registry.TabRegistry;

public class VineryLang {

    @ModLang(Mods.DV)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return TabRegistry.VINERY_TAB.get().getDisplayName();
    };

}
