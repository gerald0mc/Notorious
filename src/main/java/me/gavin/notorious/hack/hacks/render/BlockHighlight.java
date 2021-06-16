package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterValue;
import me.gavin.notorious.setting.Value;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
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

    @RegisterValue
    public final Value<Integer> red = new Value<>("Red", 255, 0, 255);

    @RegisterValue
    public final Value<Integer> green = new Value<>("Green", 255, 0, 255);

    @RegisterValue
    public final Value<Integer> blue = new Value<>("Blue", 255, 0, 255);

    @RegisterValue
    public final Value<Integer> width = new Value<>("Line Width", 2, 0, 10);

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        RayTraceResult result = mc.objectMouseOver;
        if(result != null) {
            AxisAlignedBB box = mc.world.getBlockState(result.getBlockPos()).getSelectedBoundingBox(mc.world, result.getBlockPos());
            Color color = new Color(red.value, green.value, blue.value);
            if(result.typeOfHit == RayTraceResult.Type.BLOCK && mc.world.getBlockState(result.getBlockPos()) != Blocks.AIR) {
                GL11.glLineWidth(width.value);
                RenderUtil.renderOutlineBB(box, color);
            }
        }
    }
}
