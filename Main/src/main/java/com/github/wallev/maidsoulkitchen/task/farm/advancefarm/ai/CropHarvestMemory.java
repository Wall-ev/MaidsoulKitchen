package com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai;

import net.minecraft.resources.ResourceLocation;

public record CropHarvestMemory(ResourceLocation uid, int closeDistance, CropResult cropResult) {
}
