package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.event.events.RenderEntityEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@RegisterHack(name = "Alpha", description = "ez", category = Hack.Category.Render)
public class Alpha extends Hack {
    //will make players alpha be turned down when you get close to them for when you are stacked inside of players
    @RegisterSetting private final NumSetting range = new NumSetting("Range", 4, 1, 8, 1);
    @RegisterSetting private final NumSetting a = new NumSetting("Alpha", 125, 1, 255, 0.1f);

    @SubscribeEvent
    public void onRenderPre(RenderEntityEvent.Pre event) {
        if(shouldRender(event.getEntity())) {
            GL11.glColor4f(1f, 1f, 1f, a.getValue() / 255f);
        }
    }

    @SubscribeEvent
    public void onRenderPost(RenderEntityEvent.Post event) {
        if(shouldRender(event.getEntity())) {
            GL11.glColor4f(1f, 1f, 1f, 1f);
        }
    }

    public boolean shouldRender(Entity entity) {
        if(entity == mc.player) return false;
        if(!(entity instanceof EntityPlayer)) return false;
        if(entity.getDistance(mc.player) < range.getValue()) return true;
        return false;
    }
}
