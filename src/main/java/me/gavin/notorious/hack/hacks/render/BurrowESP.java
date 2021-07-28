package me.gavin.notorious.hack.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gerald0mc and gav06
 * @since 7/18/21
 * #TODO fix the echest shit
 */

@RegisterHack(name = "BurrowESP", description = "ez", category = Hack.Category.Render)
public class BurrowESP extends Hack {

    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Both", "Both", "Outline", "Box");
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new Color(117, 0, 255, 255));
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new Color(117, 0, 255, 65));
    @RegisterSetting
    public final BooleanSetting self = new BooleanSetting("Self", true);
    @RegisterSetting
    public final BooleanSetting obsidian = new BooleanSetting("Obsidian", true);
    @RegisterSetting
    public final BooleanSetting echest = new BooleanSetting("EChest", true);
    @RegisterSetting
    public final BooleanSetting skull = new BooleanSetting("Skull", true);
    @RegisterSetting
    public final BooleanSetting anvil = new BooleanSetting("Anvil", true);
    @RegisterSetting
    public final BooleanSetting sand = new BooleanSetting("Sand", false);

    public BlockPos pos;
    public boolean fill;
    public boolean outline;
    public List<BlockPos> burrowedEntities = new ArrayList<>();

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + mode.getMode() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onTick(PlayerLivingUpdateEvent event) {
        burrowedEntities.clear();
        for(Entity e : mc.world.loadedEntityList) {
            if(e.equals(mc.player) && !self.isEnabled())
                return;
            pos = new BlockPos(e.posX, e.posY + 0.2D, e.posZ);
            if(mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN && obsidian.isEnabled()
                    || mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST && echest.isEnabled()
                    || mc.world.getBlockState(pos).getBlock() == Blocks.SKULL && skull.isEnabled()
                    || mc.world.getBlockState(pos).getBlock() == Blocks.ANVIL && anvil.isEnabled()
                    || mc.world.getBlockState(pos).getBlock() == Blocks.SAND && sand.isEnabled()) {
                burrowedEntities.add(pos);
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
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
        for(BlockPos blockPos : burrowedEntities) {
            if(outline)
                RenderUtil.renderOutlineBB(new AxisAlignedBB(blockPos), outlineColor.getAsColor());
            if(fill)
                RenderUtil.renderFilledBB(new AxisAlignedBB(blockPos), boxColor.getAsColor());
        }
    }
}
