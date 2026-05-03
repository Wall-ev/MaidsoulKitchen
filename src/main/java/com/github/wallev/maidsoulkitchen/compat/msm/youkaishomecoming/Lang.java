package com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;

public class Lang {

    @ModLang(Mods.YHCD)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return YoukaisHomecoming.TAB.get().getDisplayName();
    };

}
