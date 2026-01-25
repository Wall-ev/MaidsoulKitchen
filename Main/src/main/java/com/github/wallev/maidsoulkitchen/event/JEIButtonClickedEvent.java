package com.github.wallev.maidsoulkitchen.event;

import com.github.wallev.maidsoulkitchen.lib.auto.event.AutoEventSubscriber;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@AutoEventSubscriber(mods = Mods.JEI)
public class JEIButtonClickedEvent {

    @SubscribeEvent
    public static void autoGiveFoodVars(ScreenEvent.MouseButtonPressed.Pre event) {
        Screen screen = event.getScreen();
        if (screen instanceof RecipesGui recipesGui) {

        } else {

        }
    }

}
