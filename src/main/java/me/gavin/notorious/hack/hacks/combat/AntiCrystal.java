package me.gavin.notorious.hack.hacks.combat;

import jdk.nashorn.internal.ir.Block;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@RegisterHack(name = "AntiCrystal", description = "ez", category = Hack.Category.Combat)
public class AntiCrystal extends Hack {

    @RegisterSetting
    public final NumSetting delay = new NumSetting("Delay", 2, 1, 20, 1);
    @RegisterSetting
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    @RegisterSetting
    public final BooleanSetting packet = new BooleanSetting("Packet", false);
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new NColor(255, 255, 255, 125));
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new NColor(255, 255, 255, 255));

    public int slot;
    public TickTimer timer;
    public BlockPos crystalPos;

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
       if(crystalPos == null)
           return;
        for(Entity e : mc.world.loadedEntityList) {
            if(e instanceof EntityEnderCrystal) {
                crystalPos = new BlockPos(e.getPosition());
                slot = InventoryUtil.getItemSlot(Items.STRING);
                if(slot == -1) {
                    notorious.messageManager.sendMessage("No string in your hotbar please retry.");
                    toggle();
                }
                if(slot != -1) {
                    InventoryUtil.switchToSlot(slot);
                    if(mc.player.getHeldItemMainhand().getItem() == Items.STRING) {
                        BlockUtil.placeBlock(crystalPos, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), false);
                        crystalPos = null;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(crystalPos != null) {
            AxisAlignedBB bb = new AxisAlignedBB(crystalPos);
            RenderUtil.renderFilledBB(bb, boxColor.getAsColor());
            RenderUtil.renderOutlineBB(bb, outlineColor.getAsColor());
        }
    }
}
