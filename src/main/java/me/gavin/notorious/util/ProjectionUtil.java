package me.gavin.notorious.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class ProjectionUtil {
    private final static Minecraft mc = Minecraft.getMinecraft();
    private final static Matrix4f modelMatrix = new Matrix4f();
    private final static Matrix4f projectionMatrix = new Matrix4f();
    static Vec3d camPos = new Vec3d(0.0, 0.0, 0.0);

    public static void updateMatrix() {
        if (mc.getRenderViewEntity() == null) return;
        final Vec3d viewerPos = ActiveRenderInfo.projectViewFromEntity(mc.getRenderViewEntity(), mc.getRenderPartialTicks());
        final Vec3d relativeCamPos = ActiveRenderInfo.getCameraPosition();

        loadMatrix(modelMatrix, GL_MODELVIEW_MATRIX);
        loadMatrix(projectionMatrix, GL_PROJECTION_MATRIX);
        camPos = viewerPos.add(relativeCamPos);
    }

    private static void loadMatrix(Matrix4f matrix, int glBit) {
        final FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);
        glGetFloat(glBit, floatBuffer);
        matrix.load(floatBuffer);
    }

    public static Vec3d toScaledScreenPos(Vec3d posIn) {
        final Vector4f vector4f = getTransformedMatrix(posIn);

        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        vector4f.x = width / 2f + (0.5f * vector4f.x * width + 0.5f);
        vector4f.y = height / 2f - (0.5f * vector4f.y * height + 0.5f);
        final double posZ = isVisible(vector4f, width, height) ? 0.0 : -1.0;

        return new Vec3d(vector4f.x, vector4f.y, posZ);
    }

    public static Vec3d toScreenPos(Vec3d posIn) {
        final Vector4f vector4f = getTransformedMatrix(posIn);

        final int width = mc.displayWidth;
        final int height = mc.displayHeight;

        vector4f.x = width / 2f + (0.5f * vector4f.x * width + 0.5f);
        vector4f.y = height / 2f - (0.5f * vector4f.y * height + 0.5f);
        final double posZ = isVisible(vector4f, width, height) ? 0.0 : -1.0;

        return new Vec3d(vector4f.x, vector4f.y, posZ);
    }

    private static Vector4f getTransformedMatrix(Vec3d posIn) {
        final Vec3d relativePos = camPos.subtract(posIn);
        final Vector4f vector4f = new Vector4f((float)relativePos.x, (float)relativePos.y, (float)relativePos.z, 1f);

        transform(vector4f, modelMatrix);
        transform(vector4f, projectionMatrix);

        if (vector4f.w > 0.0f) {
            vector4f.x *= -100000;
            vector4f.y *= -100000;
        } else {
            final float invert = 1f / vector4f.w;
            vector4f.x *= invert;
            vector4f.y *= invert;
        }

        return vector4f;
    }

    private static void transform(Vector4f vec, Matrix4f matrix) {
        final float x = vec.x;
        final float y = vec.y;
        final float z = vec.z;
        vec.x = x * matrix.m00 + y * matrix.m10 + z * matrix.m20 + matrix.m30;
        vec.y = x * matrix.m01 + y * matrix.m11 + z * matrix.m21 + matrix.m31;
        vec.z = x * matrix.m02 + y * matrix.m12 + z * matrix.m22 + matrix.m32;
        vec.w = x * matrix.m03 + y * matrix.m13 + z * matrix.m23 + matrix.m33;
    }

    // stolen from a decompiler kek
    private static boolean isVisible(Vector4f pos, int width, int height) {
        double var6 = width;
        double var4 = pos.x;
        if (var4 >= 0.0D && var4 <= var6) {
            var6 = height;
            var4 = pos.y;
            return var4 >= 0.0D && var4 <= var6;
        }

        return false;
    }
}