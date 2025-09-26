package com.github.wallev.maidsoulkitchen.compat.msm.meadow;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.satisfy.meadow.core.registry.TabRegistry;

public class MeadowLang {

    @ModLang(Mods.DM)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return TabRegistry.MEADOW_TAB.get().getDisplayName();
    };

}
