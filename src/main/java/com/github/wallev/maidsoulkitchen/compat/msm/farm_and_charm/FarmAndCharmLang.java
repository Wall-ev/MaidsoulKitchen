package com.github.wallev.maidsoulkitchen.compat.msm.farm_and_charm;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.satisfy.farm_and_charm.core.registry.TabRegistry;

public class FarmAndCharmLang {

    @ModLang(Mods.DFC)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return TabRegistry.FARM_AND_CHARM_TAB.get().getDisplayName();
    };

}
