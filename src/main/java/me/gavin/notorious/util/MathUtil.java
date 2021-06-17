package me.gavin.notorious.util;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

public class MathUtil {

    public static float normalize(float value, float min, float max) {
        return 1.0f - ((value - min) / (max - min));
    }

    public static float[] calculateLookAt(double x, double y, double z, EntityPlayer me) {
        double dirx = me.posX - x;
        double diry = me.posY + me.getEyeHeight() - y;
        double dirz = me.posZ - z;

        double distance = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= distance;
        diry /= distance;
        dirz /= distance;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        pitch = pitch * 180.0f / Math.PI;
        yaw = yaw * 180.0f / Math.PI;

        yaw += 90.0f;

        return new float[] {(float)yaw, (float)pitch};
    }
}
