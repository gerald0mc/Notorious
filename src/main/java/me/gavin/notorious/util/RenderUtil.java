package me.gavin.notorious.util;

import me.gavin.notorious.stuff.IMinecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

import java.awt.*;

public class RenderUtil implements IMinecraft {

    public static void prepare() {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
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
        GlStateManager.enableLighting();
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

    public static void entityESPBox(Entity entity, Color boxC, Color outlineC, int lineWidth) {
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
        GL11.glLineWidth(lineWidth);
        RenderGlobal.renderFilledBox(lerpBox, boxC.getRed() / 255f, boxC.getGreen() / 255f, boxC.getBlue() / 255f, boxC.getAlpha() / 255f);
        RenderGlobal.drawSelectionBoundingBox(lerpBox, outlineC.getRed() / 255f, outlineC.getGreen() / 255f, outlineC.getBlue() / 255f, outlineC.getAlpha() / 255f);
        release();
    }

    public static void drawPenis(final EntityPlayer player, final double x, final double y, final double z, float pspin, float pcumsize, float pamount) {
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-player.rotationYaw, 0.0f, player.height, 0.0f);
        GL11.glTranslated(-x, -y, -z);
        GL11.glTranslated(x, y + player.height / 2.0f - 0.22499999403953552, z);
        GL11.glColor4f(1.38f, 0.55f, 2.38f, 1.0f);
        GL11.glRotated((player.isSneaking() ? 35 : 0) + pspin, 1.0f + pspin, 0.0f, pcumsize);
        GL11.glTranslated(0.0, 0.0, 0.07500000298023224);
        final Cylinder shaft = new Cylinder();
        shaft.setDrawStyle(100013);
        shaft.draw(0.1f, 0.11f, 0.4f, 25, 20);
        GL11.glTranslated(0.0, 0.0, -0.12500000298023223);
        GL11.glTranslated(-0.09000000074505805, 0.0, 0.0);
        final Sphere right = new Sphere();
        right.setDrawStyle(100013);
        right.draw(0.14f, 10, 20);
        GL11.glTranslated(0.16000000149011612, 0.0, 0.0);
        final Sphere left = new Sphere();
        left.setDrawStyle(100013);
        left.draw(0.14f, 10, 20);
        GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated(-0.07000000074505806, 0.0, 0.589999952316284);
        final Sphere tip = new Sphere();
        tip.setDrawStyle(100013);
        tip.draw(0.13f, 15, 20);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
    }

    private enum RenderMode {
        FILLED,
        OUTLINE,
    }
}