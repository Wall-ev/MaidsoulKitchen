package com.github.wallev.maidsoulkitchen.util;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;

public final class EntityUtil {

    public static float getYawFromFacing(Direction currentFacing) {
        switch (currentFacing) {
            case DOWN:
            case UP:
            case SOUTH:
            default:
                return 0;
            case EAST:
                return 270F;
            case NORTH:
                return 180F;
            case WEST:
                return 90F;
        }
    }

    public static void setEntityFacing(LivingEntity entity, Direction currentFacing) {
        float yaw = 0;
        switch (currentFacing) {
            case EAST:
                yaw = 270F;
                break;
            case NORTH:
                yaw = 180F;
                break;
            case WEST:
                yaw = 90F;
                break;
            case DOWN:
            case UP:
            case SOUTH:
            default:
                yaw = 0;
        }
        entity.setYRot(yaw);
    }

}
