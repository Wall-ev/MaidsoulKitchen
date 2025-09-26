package com.github.wallev.maidsoulkitchen.compat.msm.beachparty;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.satisfy.beachparty.core.registry.TabRegistry;

public class BeachPartyLang {

    @ModLang(Mods.DBP)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return TabRegistry.BEACHPARTY_TAB.get().getDisplayName();
    };

}
