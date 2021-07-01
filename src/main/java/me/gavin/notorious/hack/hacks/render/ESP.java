package me.gavin.notorious.hack.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.NColor;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@RegisterHack(name = "ESP", description = "Draws a box around entities.", category = Hack.Category.Render)
public class ESP extends Hack {

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

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + lineWidth.getValue() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            AxisAlignedBB box = e.getEntityBoundingBox();
            double x = (e.posX - e.lastTickPosX) * event.getPartialTicks();
            double y = (e.posY - e.lastTickPosY) * event.getPartialTicks();
            double z = (e.posZ - e.lastTickPosZ) * event.getPartialTicks();
            AxisAlignedBB bb = new AxisAlignedBB(box.minX + x, box.minY + y, box.minZ + z, box.maxX + x, box.maxY + y, box.maxZ + z);
            if(e == mc.player && mc.gameSettings.thirdPersonView == 0)
                continue;
            if(e instanceof EntityPlayer && players.isEnabled()) {
                render(e);
            } else if(e instanceof EntityAnimal && animals.isEnabled()) {
                render(e);
            } else if((e instanceof EntityMob || e instanceof EntitySlime) && mobs.isEnabled()) {
                render(e);
            } else if(e instanceof EntityItem && items.isEnabled()) {
                render(e);
            }
        }
    }

    private void render(Entity entity) {
        GlStateManager.pushMatrix();
        final AxisAlignedBB b = entity.getEntityBoundingBox().offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        double x = ((b.maxX - b.minX) / 2) + b.minX;
        double y = ((b.maxY - b.minY) / 2) + b.minY;
        double z = ((b.maxZ - b.minZ) / 2) + b.minZ;
        GL11.glTranslated(x, y, z);
        GL11.glRotated(-MathHelper.clampedLerp(entity.prevRotationYaw, entity.rotationYaw, mc.getRenderPartialTicks()), 0.0, 1.0, 0.0);
        GL11.glTranslated(-(x), -y, -z);
        RenderUtil.entityESPBox(entity, boxColor.getAsColor(), outlineColor.getAsColor(), (int) lineWidth.getValue());
        GlStateManager.popMatrix();
    }
}