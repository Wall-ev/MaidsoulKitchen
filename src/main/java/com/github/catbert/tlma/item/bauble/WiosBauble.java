package com.github.catbert.tlma.item.bauble;

import com.github.catbert.tlma.api.ILittleMaidBauble;
import com.github.catbert.tlma.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

@LittleMaidExtension
public class WiosBauble implements ILittleMaidBauble {
    @Override
    public boolean canLoaded() {
        return true;
    }

    @Override
    public @NotNull RegistryObject<Item> getBindingItem() {
        return InitItems.WIOS_BAUBLE;
    }
}
