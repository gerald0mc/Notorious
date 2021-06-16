package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.NColor;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@RegisterHack(name = "BlockHighlight", description = "Draws a bounding box around boxes you are looking at.", category = Hack.Category.Render, bind = Keyboard.KEY_K)
public class BlockHighlight extends Hack {

    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new NColor(255, 255, 255));

    @RegisterSetting
    public final NumSetting lineWidth = new NumSetting("Line Width", 2, 0.1f, 10, 0.1f);

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        RayTraceResult result = mc.objectMouseOver;
        if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            AxisAlignedBB box = mc.world.getBlockState(result.getBlockPos()).getSelectedBoundingBox(mc.world, result.getBlockPos());
            if(result.typeOfHit == RayTraceResult.Type.BLOCK && mc.world.getBlockState(result.getBlockPos()).getBlock() != Blocks.AIR) {
                GL11.glLineWidth(lineWidth.getValue());
                RenderUtil.renderOutlineBB(box, outlineColor.getAsColor());
            }
        }
    }
}
