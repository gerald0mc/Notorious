package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.event.events.RenderEntityEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
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

    @RegisterSetting
    private final BooleanSetting players = new BooleanSetting("Players", true);
    @RegisterSetting
    private final BooleanSetting animals = new BooleanSetting("Animals", false);
    @RegisterSetting
    private final BooleanSetting mobs = new BooleanSetting("Mobs", false);
    @RegisterSetting
    private final BooleanSetting walls = new BooleanSetting("Walls", true);
    @RegisterSetting
    private final BooleanSetting texture = new BooleanSetting("Texture", true);
    @RegisterSetting
    private final NumSetting lineWidth = new NumSetting("Line Width", 1f, 0.1f, 3f, 0.1f);
    @RegisterSetting
    private final ColorSetting color = new ColorSetting("Color", Color.CYAN);
    @RegisterSetting
    private final ModeSetting mode = new ModeSetting("Style", "Normal", "Normal", "Wireframe", "Color");

    @SubscribeEvent
    public void onRenderEntity(RenderEntityEvent.Pre event) {
        if (shouldDoChams(event.getEntity())) {
            setupChams();
            if (walls.isEnabled())
                startDepthRange();
        }
    }

    @SubscribeEvent
    public void onRenderEntityPost(RenderEntityEvent.Post event) {
        if (shouldDoChams(event.getEntity())) {
            if (walls.isEnabled())
                endDepthRange();
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
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(lineWidth.getValue());
        this.glColor();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
    }

    private void endWireframe() {
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    private void startColor() {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
        this.glColor();
    }

    private void endColor() {
        GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glPopMatrix();
    }

    private void setupChams() {
        if (!texture.getValue() && mode.getMode() != "Normal")
            GL11.glDisable(GL11.GL_TEXTURE_2D);

        mc.getRenderManager().setRenderShadow(false);
        if (walls.getValue())
            startDepthRange();

        if (mode.getMode() == "Wireframe")
            startWireframe();
        else if (mode.getMode() == "Color")
            startColor();
    }

    private void endChams() {
        mc.getRenderManager().setRenderShadow(mc.gameSettings.entityShadows);
        if (walls.getValue())
            endDepthRange();

        if (mode.getMode() == "Wireframe")
            endWireframe();
        else if (mode.getMode() == "Color")
            endColor();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private boolean shouldDoChams(Entity entity) {
        if (entity instanceof EntityPlayer && players.getValue()) {
            return true;
        } else if ((entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValue()) {
            return true;
        } else return entity instanceof EntityAnimal && animals.getValue();
    }

    private void glColor() {
        final Color clr = color.getAsColor();
        GL11.glColor4f(clr.getRed() / 255f, clr.getGreen() / 255f, clr.getBlue() / 255f, color.getAlpha().getValue());
    }
}
