package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.event.events.RenderEntityEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;


// TODO: Fix opacity for color mode, and change mixin
@RegisterHack(
        name = "Chams",
        description = "highlight entities",
        category = Hack.Category.Render
)
public class Chams extends Hack {

    @RegisterSetting private final BooleanSetting players = new BooleanSetting("Players", true);
    @RegisterSetting private final BooleanSetting crystal = new BooleanSetting("Crystal", true);
    @RegisterSetting private final BooleanSetting animals = new BooleanSetting("Animals", false);
    @RegisterSetting private final BooleanSetting mobs = new BooleanSetting("Mobs", false);
    @RegisterSetting private final BooleanSetting walls = new BooleanSetting("Walls", true);
    @RegisterSetting private final BooleanSetting texture = new BooleanSetting("Texture", true);
    @RegisterSetting private final BooleanSetting glint = new BooleanSetting("Glint", true);
    @RegisterSetting private final BooleanSetting rainbow = new BooleanSetting("Rainbow", false);
    @RegisterSetting private final NumSetting lineWidth = new NumSetting("Line Width", 1f, 0.1f, 3f, 0.1f);
    @RegisterSetting private final NumSetting r = new NumSetting("Red", 255, 0, 255, 1);
    @RegisterSetting private final NumSetting g = new NumSetting("Green", 255, 0, 255, 1);
    @RegisterSetting private final NumSetting b = new NumSetting("Blue", 255, 0, 255, 1);
    @RegisterSetting private final NumSetting a = new NumSetting("Alpha", 255, 0, 255, 1);
    @RegisterSetting private final ModeSetting mode = new ModeSetting("RenderMode", "Color", "Color", "Wireframe", "Skin");

    public Entity entityBeingRendered;

    @SubscribeEvent
    public void onRenderEntity(RenderEntityEvent.Pre event) {
        if (shouldDoChams(event.getEntity())) {
            setupChams();
            entityBeingRendered = event.getEntity();
            if (walls.isEnabled())
                startDepthRange();
        }
    }

    @SubscribeEvent
    public void onRenderEntityPost(RenderEntityEvent.Post event) {
        if (shouldDoChams(event.getEntity())) {
            if (walls.isEnabled())
                endDepthRange();
            entityBeingRendered = null;
            endChams();
        }
    }

    private void startDepthRange() {
        GL11.glDepthRange(0.0, 0.01);
    }

    private void endDepthRange() {
        GL11.glDepthRange(0.0, 1.0);
    }

    private void startWireframe() {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(lineWidth.getValue());
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glLineWidth(lineWidth.getValue());
        GL11.glColor4f(rainbow.getValue() ? ColorUtil.getRainbow(6f, 1f) : r.getValue() / 255f, g.getValue() / 255f, b.getValue() / 255f, 1f);
    }

    private void endWireframe() {
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void startColor() {
        GL11.glPushAttrib(GL11.GL_ALL_CLIENT_ATTRIB_BITS);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glColor4f(rainbow.getValue() ? ColorUtil.getRainbow(6f, 1f) : r.getValue() / 255f, g.getValue() / 255f, b.getValue() / 255f, a.getValue() / 255f);
    }

    private void endColor() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glPopAttrib();
    }

    private void setupChams() {
        if (!texture.getValue() && mode.getMode() != "Skin")
            GL11.glDisable(GL11.GL_TEXTURE_2D);

        mc.getRenderManager().setRenderShadow(false);
        if (walls.getValue())
            startDepthRange();

        if (mode.getMode().equals("Wireframe")) {
            startWireframe();
        }else if (mode.getMode().equals("Color")) {
            startColor();
        }else if(mode.getMode().equals("Both")) {
            startWireframe();
            startColor();
        }
    }

    private void endChams() {
        mc.getRenderManager().setRenderShadow(mc.gameSettings.entityShadows);
        if (walls.getValue())
            endDepthRange();

        if (mode.getMode() == "Wireframe")
            endWireframe();
        else if (mode.getMode() == "Color")
            endColor();
        else if(mode.getMode() == "Both") {
            endColor();
            endWireframe();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private boolean shouldDoChams(Entity entity) {
        if (entity instanceof EntityPlayer && players.getValue()) {
            return true;
        } else if ((entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValue()) {
            return true;
        }else if(entity instanceof EntityEnderCrystal && crystal.getValue()) {
            return true;
        }else return entity instanceof EntityAnimal && animals.getValue();
    }
}
