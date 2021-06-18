package me.gavin.notorious.util;

import me.gavin.notorious.stuff.IMinecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtil implements IMinecraft {

    public static void prepare() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
    }

    public static void release() {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void renderFilledBB(AxisAlignedBB box, Color color) {
        renderBB(box, color, RenderMode.FILLED);
    }

    public static void renderOutlineBB(AxisAlignedBB box, Color color) {
        renderBB(box, color, RenderMode.OUTLINE);
    }

    public static void renderBB(AxisAlignedBB box, Color color, RenderMode mode) {
        prepare();
        final float r = color.getRed() / 255f;
        final float g = color.getGreen() / 255f;
        final float b = color.getBlue() / 255f;
        final float a = color.getAlpha() / 255f;
        box = box.offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);

        if (mode == RenderMode.FILLED)
            RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
        else
            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
        release();
    }

    public static void entityESPBox(Entity entity, Color c) {
        final AxisAlignedBB ebox = entity.getEntityBoundingBox();

        final double lerpX = MathUtil.lerp(mc.getRenderPartialTicks(), entity.lastTickPosX, entity.posX);
        final double lerpY = MathUtil.lerp(mc.getRenderPartialTicks(), entity.lastTickPosY, entity.posY);
        final double lerpZ = MathUtil.lerp(mc.getRenderPartialTicks(), entity.lastTickPosZ, entity.posZ);

        final AxisAlignedBB lerpBox = new AxisAlignedBB(
                ebox.minX - 0.05 - lerpX + (lerpX - mc.getRenderManager().viewerPosX),
                ebox.minY - lerpY + (lerpY - mc.getRenderManager().viewerPosY),
                ebox.minZ - 0.05 - lerpZ + (lerpZ - mc.getRenderManager().viewerPosZ),
                ebox.maxX + 0.05 - lerpX + (lerpX - mc.getRenderManager().viewerPosX),
                ebox.maxY + 0.1 - lerpY + (lerpY - mc.getRenderManager().viewerPosY),
                    ebox.maxZ + 0.05 - lerpZ + (lerpZ - mc.getRenderManager().viewerPosZ)
        );

        prepare();
        GL11.glLineWidth(1.0f);
        RenderGlobal.renderFilledBox(lerpBox, c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        RenderGlobal.drawSelectionBoundingBox(lerpBox, c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        release();
    }

    private enum RenderMode {
        FILLED,
        OUTLINE,
    }
}