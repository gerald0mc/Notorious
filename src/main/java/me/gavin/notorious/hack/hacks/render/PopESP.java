package me.gavin.notorious.hack.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.RenderEntityEvent;
import me.gavin.notorious.event.events.TotemPopEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@RegisterHack(name = "PopESP", description = "Renders pops", category = Hack.Category.Render)
public class PopESP extends Hack {

    @RegisterSetting
    public final NumSetting fadeTime = new NumSetting("FadeTime", 3000, 1, 5000, 100);
    @RegisterSetting
    public final NumSetting fadeSpeed = new NumSetting("FadeSpeed", 0.05f, 0.01f, 1.0f, 0.01f);
    @RegisterSetting
    public final ModeSetting fadeMode = new ModeSetting("FadeMode", "Elevator", "Elevator", "Fade", "None");
    @RegisterSetting
    public final ModeSetting elevatorMode = new ModeSetting("ElevatorMode", "Heaven", "Heaven", "Hell");
    @RegisterSetting
    public final ModeSetting renderMode = new ModeSetting("RenderMode", "Both", "Both", "Textured", "Wireframe");
    @RegisterSetting
    private final NumSetting lineWidth = new NumSetting("Line Width", 1f, 0.1f, 3f, 0.1f);
    @RegisterSetting
    public final NumSetting r = new NumSetting("Red", 255, 0, 255, 1);
    @RegisterSetting
    public final NumSetting g = new NumSetting("Green", 255, 0, 255, 1);
    @RegisterSetting
    public final NumSetting b = new NumSetting("Blue", 255, 0, 255, 1);
    @RegisterSetting
    public final NumSetting a = new NumSetting("Alpha", 255, 0, 255, 1);

    private static final HashMap<EntityOtherPlayerMP, Long> popFakePlayerMap = new HashMap<>();

    float fade = 1.0f;

    @Override
    public String getMetaData() {
        if(fadeMode.getMode().equals("Elevator")) {
            return " [" + ChatFormatting.GRAY + elevatorMode.getMode() + ChatFormatting.RESET + "]";
        }else {
            return " [" + ChatFormatting.GRAY + fadeMode.getMode() + ChatFormatting.RESET + "]";
        }
    }

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        for (Map.Entry<EntityOtherPlayerMP, Long> entry : new HashMap<>(popFakePlayerMap).entrySet()) {
            boolean wireFrame;
            boolean textured;
            if(renderMode.getMode().equals("Both")) {
                wireFrame = true;
                textured = true;
            }else if(renderMode.getMode().equals("Wireframe")) {
                wireFrame = true;
                textured = false;
            }else {
                wireFrame = false;
                textured = true;
            }
            if(System.currentTimeMillis() - entry.getValue() < (long) fadeTime.getValue() && fadeMode.getMode().equals("Elevator")) {
                if(elevatorMode.getMode().equals("Heaven")) {
                    entry.getKey().posY += fadeSpeed.getValue() * event.getPartialTicks();
                }else {
                    entry.getKey().posY -= fadeSpeed.getValue() * event.getPartialTicks();
                }
            }else if(System.currentTimeMillis() - entry.getValue() < (long) fadeTime.getValue() && fadeMode.getMode().equals("Fade")) {
                fade -= fadeSpeed.getValue();
            }
            if(System.currentTimeMillis() - entry.getValue() > (long) fadeTime.getValue() || fade == 0.0f) {
                popFakePlayerMap.remove(entry.getKey());
                continue;
            }
            GL11.glPushMatrix();
            GL11.glDepthRange(0.01, 1.0f);
            if(wireFrame) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glLineWidth(lineWidth.getValue());
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
                GL11.glColor4f(r.getValue() / 255f, g.getValue() / 255f, b.getValue() / 255f, fadeMode.getMode().equals("Fade") ? fade : 1f);
                renderEntityStatic(entry.getKey(), event.getPartialTicks(), true);
                GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glColor4f(1f, 1f, 1f, 1f);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            if(textured) {
                GL11.glPushAttrib(GL11.GL_ALL_CLIENT_ATTRIB_BITS);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GL11.glLineWidth(1.5f);
                GL11.glColor4f(r.getValue() / 255f, g.getValue() / 255f, b.getValue() / 255f, fadeMode.getMode().equals("Fade") ? fade : a.getValue() / 255f);
                renderEntityStatic(entry.getKey(), event.getPartialTicks(), true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1f, 1f, 1f, 1f);
                GL11.glPopAttrib();
            }
            GL11.glDepthRange(0.0, 1.0f);
            GL11.glPopMatrix();
            fade = 1.0f;
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

    public void renderEntityStatic(Entity entityIn, float partialTicks, boolean p_188388_3_) {
        if (entityIn.ticksExisted == 0)
        {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
        }

        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        int i = entityIn.getBrightnessForRender();

        if (entityIn.isBurning())
        {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        mc.getRenderManager().renderEntity(entityIn, d0 - mc.getRenderManager().viewerPosX, d1 - mc.getRenderManager().viewerPosY, d2 - mc.getRenderManager().viewerPosZ, f, partialTicks, p_188388_3_);
    }

    private void glColor(boolean textured, boolean wireframe) {
        final Color clr = new Color(r.getValue(), g.getValue(), b.getValue(), a.getValue());
        if(textured)
            GL11.glColor4f(clr.getRed() / 255f, clr.getGreen() / 255f, clr.getBlue() / 255f, clr.getAlpha() / 255f);
        if(wireframe)
            GL11.glColor4f(clr.getRed() / 255f, clr.getGreen() / 255f, clr.getBlue() / 255f, 1f);
    }
}