package me.gavin.notorious.util;

import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * @skidded from phobos for logoutSpots this was originally in the module but i moved it here instead because it looked ugly in there
 * https://github.com/Gopro336/Phobos_1.7.2-BUILDABLE-SRC-Non-Chinese-/blob/146757342c30334b5870d7bf44f3b3df4b094040/src/main/java/me/earth/phobos/features/modules/render/LogoutSpots.java#L158
*/
public class LogoutUtil {

    private final String name;
    private final UUID uuid;
    private final EntityPlayer entity;
    private final double x;
    private final double y;
    private final double z;

    public LogoutUtil(String name, UUID uuid, EntityPlayer entity) {
        this.name = name;
        this.uuid = uuid;
        this.entity = entity;
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public EntityPlayer getEntity() {
        return this.entity;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}
