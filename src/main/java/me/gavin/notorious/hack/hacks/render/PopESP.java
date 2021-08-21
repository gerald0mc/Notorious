package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.event.events.TotemPopEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

@RegisterHack(
        name = "PopESP",
        description = "Renders pops",
        category = Hack.Category.Render
)
public class PopESP extends Hack {

    @RegisterSetting
    public final NumSetting fadeTime = new NumSetting("FadeTime", 3000, 1, 5000, 100);
    @RegisterSetting
    public final ColorSetting color = new ColorSetting("Color", 0, 255, 255, 255);
    @RegisterSetting
    public final NumSetting alpha = new NumSetting("Alpha", 125, 0, 255, 1);

    private final HashMap<EntityOtherPlayerMP, Long> popFakePlayerMap = new HashMap<>();

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        for (Map.Entry<EntityOtherPlayerMP, Long> entry : new HashMap<>(popFakePlayerMap).entrySet()) {
            if (System.currentTimeMillis() - entry.getValue() > (long) fadeTime.getValue()) {
                popFakePlayerMap.remove(entry.getKey());
                continue;
            }
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            GL11.glColor4f(color.getAsColor().getRed() / 255f, color.getAsColor().getGreen() / 255f, color.getAsColor().getBlue() / 255f, alpha.getValue() / 255f);
            mc.getRenderManager().renderEntityStatic(entry.getKey(), event.getPartialTicks(), false);
            GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            GlStateManager.popMatrix();
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
}