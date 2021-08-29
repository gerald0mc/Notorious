package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.event.events.RenderEntityEvent;
import me.gavin.notorious.event.events.TotemPopEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@RegisterHack(name = "PopESP", description = "Renders pops", category = Hack.Category.Render)
public class PopESP extends Hack {

    @RegisterSetting
    public final NumSetting fadeTime = new NumSetting("FadeTime", 3000, 1, 5000, 100);
    @RegisterSetting
    private final NumSetting lineWidth = new NumSetting("Line Width", 1f, 0.1f, 3f, 0.1f);
    @RegisterSetting
    public final ColorSetting color = new ColorSetting("Color", 0, 255, 255, 255);

    private final HashMap<EntityOtherPlayerMP, Long> popFakePlayerMap = new HashMap<>();

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        for (Map.Entry<EntityOtherPlayerMP, Long> entry : new HashMap<>(popFakePlayerMap).entrySet()) {
            if (System.currentTimeMillis() - entry.getValue() > (long) fadeTime.getValue()) {
                popFakePlayerMap.remove(entry.getKey());
                continue;
            }
            GL11.glPushMatrix();
            GL11.glDepthRange(0.0, 0.01);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glLineWidth(lineWidth.getValue());
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
            this.glColor();
            mc.getRenderManager().renderEntityStatic(entry.getKey(), 0, false);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthRange(0.0, 1.0);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if (mc.world.getEntityByID(event.getEntityId()) != null) {
            final Entity entity = mc.world.getEntityByID(event.getEntityId());
            if (entity instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer) entity;
                final EntityOtherPlayerMP fakeEntity = new EntityOtherPlayerMP(mc.world, player.getGameProfile());
                fakeEntity.copyLocationAndAnglesFrom(player);
                fakeEntity.rotationYawHead = player.rotationYawHead;
                fakeEntity.prevRotationYawHead = player.rotationYawHead;
                fakeEntity.rotationYaw = player.rotationYaw;
                fakeEntity.prevRotationYaw = player.rotationYaw;
                fakeEntity.rotationPitch = player.rotationPitch;
                fakeEntity.prevRotationPitch = player.rotationPitch;
                fakeEntity.cameraYaw = fakeEntity.rotationYaw;
                fakeEntity.cameraPitch = fakeEntity.rotationPitch;
                popFakePlayerMap.put(fakeEntity, System.currentTimeMillis());
            }
        }
    }

    private void glColor() {
        final Color clr = color.getAsColor();
        GL11.glColor4f(clr.getRed() / 255f, clr.getGreen() / 255f, clr.getBlue() / 255f, color.getAlpha().getValue());
    }

//    public void renderEntity(Entity entityIn, float partialTicks, boolean p_188388_3_) {
//        if (entityIn.ticksExisted == 0) {
//            entityIn.lastTickPosX = entityIn.posX;
//            entityIn.lastTickPosY = entityIn.posY;
//            entityIn.lastTickPosZ = entityIn.posZ;
//        }
//
//        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
//        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
//        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
//        float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
//        int i = entityIn.getBrightnessForRender();
//
//        if (entityIn.isBurning()) {
//            i = 15728880;
//        }
//
//        int j = i % 65536;
//        int k = i / 65536;
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
//        mc.getRenderManager().renderEntity(entityIn, d0 - mc.getRenderManager().viewerPosX, d1 - mc.getRenderManager().viewerPosY, d2 - mc.getRenderManager().viewerPosZ, f, partialTicks, p_188388_3_);
//    }
}