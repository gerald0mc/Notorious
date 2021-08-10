package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.util.BlockUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@RegisterHack(name = "BurrowBreaker", description = "ez", category = Hack.Category.Combat)
public class BurrowBreaker extends Hack {

    @RegisterSetting
    public final BooleanSetting packet = new BooleanSetting("Packet", false);
    @RegisterSetting
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);

    private boolean isBreaking = false;
    public BlockPos pos;
    public List<BlockPos> burrowedEntities = new ArrayList<>();

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        burrowedEntities.clear();
        for(Entity e : mc.world.loadedEntityList) {
            if(e instanceof EntityPlayer) {
                if(e.equals(mc.player))
                    return;
                pos = new BlockPos(e.posX, e.posY + 0.2D, e.posZ);
                if(mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST) {
                    burrowedEntities.add(pos);
                }
                if(burrowedEntities.contains(pos) && mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe && !isBreaking) {
                    BlockUtil.damageBlock(pos, packet.getValue(), rotate.getValue());
                    isBreaking = true;
                }
                if(mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
                    isBreaking = false;
                }
            }
        }
    }
}
