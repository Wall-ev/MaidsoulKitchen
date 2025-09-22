package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.menu;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class IWrapMenu<T extends AbstractContainerMenu> {
    static Map<ResourceLocation, IWrapMenu<?>> MAP = new HashMap<>();

    private final ResourceLocation id;

    public IWrapMenu(ResourceLocation id) {
        MAP.put(id, this);
        this.id = id;
    }

    public static IWrapMenu<?> get(ResourceLocation id) {
        return MAP.get(id);
    }

    @SuppressWarnings("unchecked")
    public T createMenu(int containerId, Inventory playerInv) {
        return (T) Objects.requireNonNull(ForgeRegistries.MENU_TYPES.getValue(this.id)).create(containerId, playerInv);
    }

    public abstract boolean isValidSlot(@Nullable Direction direction, int slot);
}
