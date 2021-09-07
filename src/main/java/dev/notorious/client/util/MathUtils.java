package dev.notorious.client.util;

import net.minecraft.util.math.Vec3d;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
    public static double square(double input){
        return input * input;
    }

    public static Vec3d roundVector(Vec3d vec3d, int places){
        return new Vec3d(round(vec3d.x, places), round(vec3d.y, places), round(vec3d.z, places));
    }

    public static float round(float value, int places){
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(places, RoundingMode.FLOOR);
        return decimal.floatValue();
    }

    public static double round(double value, int places){
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(places, RoundingMode.FLOOR);
        return decimal.doubleValue();
    }
}
