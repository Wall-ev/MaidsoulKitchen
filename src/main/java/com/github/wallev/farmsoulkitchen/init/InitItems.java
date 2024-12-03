package com.github.wallev.farmsoulkitchen.init;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.item.ItemCulinaryHub;
import com.github.tartaricacid.touhoulittlemaid.item.ItemDamageableBauble;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidBackpack;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FarmsoulKitchen.MOD_ID);

    public static RegistryObject<Item> OLD_MAID_BACKPACK_BIG = ITEMS.register("old_maid_backpack_big", ItemMaidBackpack::new);
    public static RegistryObject<Item> BURN_PROTECT_BAUBLE = ITEMS.register("burn_protect_bauble", () -> new ItemDamageableBauble(128));
    public static RegistryObject<Item> CULINARY_HUB = ITEMS.register("culinary_hub", ItemCulinaryHub::new);
}
