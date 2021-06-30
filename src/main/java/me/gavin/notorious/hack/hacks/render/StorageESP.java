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
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author gerald0mc
 *
 * @since 6/17/21 at 12:13am
 */

@RegisterHack(name = "StorageESP", description = "Draws a box around storage stuff.", category = Hack.Category.Render)
public class StorageESP extends Hack {

    @RegisterSetting
    public final ModeSetting renderMode = new ModeSetting("RenderMode", "Both", "Both", "Outline", "Box");
    @RegisterSetting
    public final ModeSetting colorMode = new ModeSetting("ColorMode", "Custom", "Static", "Custom");
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new Color(255, 255, 255, 255));
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new Color(255, 255, 255, 125));
    @RegisterSetting
    public final NumSetting lineWidth = new NumSetting("LineWidth", 2f, 0.1f, 4f, 0.1f);

    private boolean outline = false;
    private boolean fill = false;

    public final Color chestOutlineStatic = new Color(139, 69, 19, 255);
    public final Color chestBoxStatic = new Color(205, 133, 63, 125);
    public final Color enderChestOutlineStatic = new Color(75, 0, 130, 255);
    public final Color enderChestBoxStatic = new Color(138, 43, 226, 125);
    public final Color hopperOutlineStatic = new Color(105, 105, 105, 255);
    public final Color hopperBoxStatic = new Color(169, 169, 169, 125);
    public final Color shulkerOutlineStatic = new Color(199, 21, 133, 255);
    public final Color shulkerBoxStatic = new Color(234, 16, 130, 125);

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + renderMode.getMode() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for(TileEntity e : mc.world.loadedTileEntityList) {
            AxisAlignedBB bb = new AxisAlignedBB(e.getPos());
            if(renderMode.getMode().equals("Both")) {
                outline = true;
                fill = true;
            }else if(renderMode.getMode().equals("Outline")) {
                outline = true;
                fill = false;
            }else {
                fill = true;
                outline = false;
            }
            if(e instanceof TileEntityChest) {
                if(colorMode.getMode().equals("Custom")) {
                    GL11.glLineWidth(lineWidth.getValue());
                    if(outline)
                        RenderUtil.renderOutlineBB(bb, outlineColor.getAsColor());
                    if(fill)
                        RenderUtil.renderFilledBB(bb, boxColor.getAsColor());
                }
                if(colorMode.getMode().equals("Static")) {
                    GL11.glLineWidth(lineWidth.getValue());
                    if(outline)
                        RenderUtil.renderOutlineBB(bb, chestOutlineStatic);
                    if(fill)
                        RenderUtil.renderFilledBB(bb, chestBoxStatic);
                }
            }
            if(e instanceof TileEntityEnderChest) {
                if(colorMode.getMode().equals("Custom")) {
                    GL11.glLineWidth(lineWidth.getValue());
                    if(outline)
                        RenderUtil.renderOutlineBB(bb, outlineColor.getAsColor());
                    if(fill)
                        RenderUtil.renderFilledBB(bb, boxColor.getAsColor());
                }
                if(colorMode.getMode().equals("Static")) {

                    GL11.glLineWidth(lineWidth.getValue());
                    if(outline)
                        RenderUtil.renderOutlineBB(bb, enderChestOutlineStatic);
                    if(fill)
                        RenderUtil.renderFilledBB(bb, enderChestBoxStatic);
                }
            }
            if(e instanceof TileEntityHopper) {
                if(colorMode.getMode().equals("Custom")) {
                    GL11.glLineWidth(lineWidth.getValue());
                    if(outline)
                        RenderUtil.renderOutlineBB(bb, outlineColor.getAsColor());
                    if(fill)
                        RenderUtil.renderFilledBB(bb, boxColor.getAsColor());
              }
                if(colorMode.getMode().equals("Static")) {
                    GL11.glLineWidth(lineWidth.getValue());
                    if(outline)
                        RenderUtil.renderOutlineBB(bb, hopperOutlineStatic);
                    if(fill)
                        RenderUtil.renderFilledBB(bb, hopperBoxStatic);
                }
            }
            if(e instanceof TileEntityShulkerBox) {
                if(colorMode.getMode().equals("Custom")) {
                    GL11.glLineWidth(lineWidth.getValue());
                    if(outline)
                        RenderUtil.renderOutlineBB(bb, outlineColor.getAsColor());
                    if(fill)
                        RenderUtil.renderFilledBB(bb, boxColor.getAsColor());
                }
                if(colorMode.getMode().equals("Static")) {
                    GL11.glLineWidth(lineWidth.getValue());
                    if(outline)
                        RenderUtil.renderOutlineBB(bb, shulkerOutlineStatic);
                    if(fill)
                        RenderUtil.renderFilledBB(bb, shulkerBoxStatic);
                }
            }
        }
    }
}
