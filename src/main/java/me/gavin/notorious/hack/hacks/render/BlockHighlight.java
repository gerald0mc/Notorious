package me.gavin.notorious.hack.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.ColorUtil;
import me.gavin.notorious.util.NColor;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import java.awt.*;

@RegisterHack(name = "BlockHighlight", description = "Draws a bounding box around boxes you are looking at.", category = Hack.Category.Render)
public class BlockHighlight extends Hack {


    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Outline", "Both", "Outline", "Box");
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("OutlineColor", new NColor(255, 255, 255));
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("BoxColor", 255, 255, 255, 125);
    @RegisterSetting
    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", true);
    @RegisterSetting
    public final NumSetting saturation = new NumSetting("Saturation", 0.6f, 0.1f, 1f, 0.1f);
    @RegisterSetting
    public final NumSetting time = new NumSetting("RainbowLength", 8, 1, 15, 1);
    @RegisterSetting
    public final NumSetting lineWidth = new NumSetting("Line Width", 2, 0.1f, 10, 0.1f);

    boolean outline = false;
    boolean fill = false;

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + lineWidth.getValue() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        RayTraceResult result = mc.objectMouseOver;
        Color rainbowColor = ColorUtil.colorRainbow((int) time.getValue(), saturation.getValue(), 1f);
        if(mode.getMode().equals("Both")) {
            outline = true;
            fill = true;
        }else if(mode.getMode().equals("Outline")) {
            outline = true;
            fill = false;
        }else {
            fill = true;
            outline = false;
        }
        if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            AxisAlignedBB box = mc.world.getBlockState(result.getBlockPos()).getSelectedBoundingBox(mc.world, result.getBlockPos());
            if(result.typeOfHit == RayTraceResult.Type.BLOCK && mc.world.getBlockState(result.getBlockPos()).getBlock() != Blocks.AIR) {
                GL11.glLineWidth(lineWidth.getValue());
                if(rainbow.isEnabled()) {
                    if(outline)
                        RenderUtil.renderOutlineBB(box, rainbowColor);
                    if(fill)
                        RenderUtil.renderFilledBB(box, rainbowColor);
                }else {
                    if(outline)
                        RenderUtil.renderOutlineBB(box, outlineColor.getAsColor());
                    if(fill)
                        RenderUtil.renderFilledBB(box, boxColor.getAsColor());
                }
            }
        }
    }
}
