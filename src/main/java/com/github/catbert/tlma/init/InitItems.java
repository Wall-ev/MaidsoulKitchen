package com.github.catbert.tlma.init;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.item.ItemWios;
import com.github.tartaricacid.touhoulittlemaid.item.ItemDamageableBauble;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidBackpack;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TLMAddon.MOD_ID);

    public static RegistryObject<Item> OLD_MAID_BACKPACK_BIG = ITEMS.register("old_maid_backpack_big", ItemMaidBackpack::new);
    public static RegistryObject<Item> BURN_PROTECT_BAUBLE = ITEMS.register("burn_protect_bauble", () -> new ItemDamageableBauble(128));
    public static RegistryObject<Item> WIOS_BAUBLE  = ITEMS.register("wios_bauble", ItemWios::new);

}
