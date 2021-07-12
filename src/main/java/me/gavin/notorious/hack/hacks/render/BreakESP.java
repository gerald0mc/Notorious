package me.gavin.notorious.hack.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.mixin.mixins.accessor.IRenderGlobal;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.ColorUtil;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@RegisterHack(name = "BreakESP", description = "shows break progress", category = Hack.Category.Render)
public class BreakESP extends Hack {

    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Both", "Both", "Outline", "Box");
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("OutlineColor", 255, 255, 255, 125);
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("BoxColor", 255, 255, 255, 125);
    @RegisterSetting
    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", true);
    @RegisterSetting
    public final NumSetting saturation = new NumSetting("Saturation", 0.6f, 0.1f, 1f, 0.1f);
    @RegisterSetting
    public final NumSetting time = new NumSetting("RainbowLength", 8, 1, 15, 1);
    @RegisterSetting
    public final NumSetting range = new NumSetting("Range", 15, 1, 20, 1);
    @RegisterSetting
    public final BooleanSetting fade = new BooleanSetting("Fade", true);

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + mode.getMode() + ChatFormatting.RESET + "]";
    }

    boolean outline = false;
    boolean fill = false;

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        ((IRenderGlobal) mc.renderGlobal).getDamagedBlocks().forEach((integer, destroyBlockProgress) -> {
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
            if(destroyBlockProgress.getPosition().getDistance((int) mc.player.posX,(int)  mc.player.posY,(int)  mc.player.posZ) <= range.getValue()) {
                AxisAlignedBB pos = mc.world.getBlockState(destroyBlockProgress.getPosition()).getSelectedBoundingBox(mc.world, destroyBlockProgress.getPosition());
                if (fade.isEnabled())
                    pos = pos.shrink((3 - (destroyBlockProgress.getPartialBlockDamage()) / (2.0 + (2.0 / 3.0))) / 9.0);
                if(outline)
                    if(rainbow.isEnabled()) {
                        RenderUtil.renderOutlineBB(pos, rainbowColor);
                    }else {
                        RenderUtil.renderOutlineBB(pos, outlineColor.getAsColor());
                    }
                if(fill)
                    if(rainbow.isEnabled()) {
                        RenderUtil.renderFilledBB(pos, rainbowColor);
                    }else {
                        RenderUtil.renderFilledBB(pos, boxColor.getAsColor());
                    }
            }
        });
    }
}
