package me.gavin.notorious.hack.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Objects;

@RegisterHack(name = "Quiver", description = "Automatically places a totem in your offhand.", category = Hack.Category.Combat)
public class Quiver extends Hack{

    @RegisterSetting
    public final NumSetting tickDelay = new NumSetting("HoldTime", 3, 0, 8, 0.5f);
    @RegisterSetting
    public final BooleanSetting autoEffect = new BooleanSetting("AutoEffect", true);

    public List<Integer> slot;
    public int speedSlot = -1;
    public int strengthSlot = -1;

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + tickDelay.getValue() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.getItemInUseMaxCount() >= tickDelay.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.cameraYaw, -90f, true));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem());
        }
        slot = InventoryUtil.getItemInventory(Items.TIPPED_ARROW);
        for (final Integer slots : slot) {
            if (PotionUtils.getPotionFromItem(Quiver.mc.player.inventory.getStackInSlot((int)slots)).getRegistryName().getPath().contains("swiftness")) {
                speedSlot = slots;
            }
            else {
                if (!Objects.requireNonNull(PotionUtils.getPotionFromItem(Quiver.mc.player.inventory.getStackInSlot((int)slots)).getRegistryName()).getPath().contains("strength")) {
                    continue;
                }
                strengthSlot = slots;
            }
        }
    }
}