package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.NColor;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@RegisterHack(name = "ESP", description = "Draws a box around entities.", category = Hack.Category.Render, bind = Keyboard.KEY_B)
public class ESP extends Hack {

    @RegisterSetting
    public final ModeSetting renderMode = new ModeSetting("RenderMode", "Both", "Both", "Outline", "Box");
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new NColor(255, 255, 255, 255));
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new NColor(255, 255, 255, 125));
    @RegisterSetting
    public final NumSetting lineWidth = new NumSetting("LineWidth", 2f, 0.1f, 4f, 0.1f);
    @RegisterSetting
    public final BooleanSetting players = new BooleanSetting("Players", true);
    @RegisterSetting
    public final BooleanSetting animals = new BooleanSetting("Animals", true);
    @RegisterSetting
    public final BooleanSetting mobs = new BooleanSetting("Mobs", true);
    @RegisterSetting
    public final BooleanSetting items = new BooleanSetting("Items", true);

    private boolean outline;
    private boolean fill;

    private AxisAlignedBB bb;

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            AxisAlignedBB box = e.getEntityBoundingBox();
            double x = (e.posX - e.lastTickPosX) * event.getPartialTicks();
            double y = (e.posY - e.lastTickPosY) * event.getPartialTicks();
            double z = (e.posZ - e.lastTickPosZ) * event.getPartialTicks();
            bb = new AxisAlignedBB(box.minX + x, box.minY + y, box.minZ + z, box.maxX + x, box.maxY + y, box.maxZ + z);
            if(e == mc.player)
                continue;
            if(renderMode.getMode().equals("Both"))
                outline = true;
                fill = true;
            if(renderMode.getMode().equals("Outline"))
                outline = true;
            if(renderMode.getMode().equals("Box"))
                fill = true;

            if(e instanceof EntityPlayer && players.isEnabled()) {
                render();
            } else if(e instanceof EntityAnimal && animals.isEnabled()) {
                render();
            } else if((e instanceof EntityMob || e instanceof EntitySlime) && mobs.isEnabled()) {
                render();
            } else if(e instanceof EntityItem && items.isEnabled()) {
                render();
            }
        }
    }

    private void render() {
        GL11.glLineWidth(lineWidth.getValue());
        if (outline)
            RenderUtil.renderOutlineBB(bb, outlineColor.getAsColor());
        if (fill)
            RenderUtil.renderFilledBB(bb, boxColor.getAsColor());
    }
}
