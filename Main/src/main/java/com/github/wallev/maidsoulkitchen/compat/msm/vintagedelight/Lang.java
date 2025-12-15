package com.github.wallev.maidsoulkitchen.compat.msm.vintagedelight;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.ribs.vintagedelight.item.ModCreativeModTabs;

public class Lang {

    @ModLang(Mods.VTD)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return ModCreativeModTabs.VINTAGE_TAB.get().getDisplayName();
    };

}
