package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@RegisterHack(name = "PenisESP", description = "ESP for your penis.", category = Hack.Category.Render)
public class PenisESP extends Hack {

    private float pspin, pcumsize, pamount;

    @Override
    public void onEnable() {
        pspin = 0;
        pcumsize = 0;
        pamount = 0;
    }

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        for (final Object o : mc.world.loadedEntityList) {
            if (o instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)o;
                final double x2 = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks();
                final double x = x2 - mc.getRenderManager().viewerPosX;
                final double y2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks();
                final double y = y2 - mc.getRenderManager().viewerPosY;
                final double z2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks();
                final double z = z2 - mc.getRenderManager().viewerPosZ;
                GL11.glPushMatrix();
                RenderHelper.disableStandardItemLighting();
                RenderUtil.drawPenis(player, x, y, z, pspin, pcumsize, pamount);
                RenderHelper.enableStandardItemLighting();
                GL11.glPopMatrix();
            }
        }
    }
}
