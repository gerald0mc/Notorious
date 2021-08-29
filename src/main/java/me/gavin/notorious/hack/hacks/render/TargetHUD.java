package me.gavin.notorious.hack.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.hack.hacks.combat.AutoCrystal;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.ColorUtil;
import me.gavin.notorious.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;

@RegisterHack(name = "TargetHUD", description = "ez", category = Hack.Category.Render)
public class TargetHUD extends Hack {

    @RegisterSetting
    public final NumSetting x = new NumSetting("X", 2.0f, 0.1f, 1000.0f, 0.1f);
    @RegisterSetting
    public final NumSetting y = new NumSetting("Y", 2.0f, 0.1f, 600.0f, 0.1f);
    @RegisterSetting
    public final NumSetting range = new NumSetting("Range", 4, 1, 6, 1);
    @RegisterSetting
    public final BooleanSetting autoCrystalTarget = new BooleanSetting("AutoCrystalTarget", true);
    @RegisterSetting
    public final BooleanSetting friendSkip = new BooleanSetting("FriendSkip", false);
    @RegisterSetting
    public final BooleanSetting background = new BooleanSetting("Background", true);
    @RegisterSetting
    public final BooleanSetting rainbowLine = new BooleanSetting("RainbowLine", true);
    @RegisterSetting
    public final BooleanSetting name = new BooleanSetting("Name", true);
    @RegisterSetting
    public final BooleanSetting health = new BooleanSetting("Health", true);
    @RegisterSetting
    public final BooleanSetting playerView = new BooleanSetting("PlayerView", true);
    @RegisterSetting
    public final BooleanSetting ping = new BooleanSetting("Ping", true);
    @RegisterSetting
    public final BooleanSetting fucked = new BooleanSetting("FuckedDetector", true);
    @RegisterSetting
    public final BooleanSetting totemPopCounter = new BooleanSetting("TotemPopCounter", true);
    @RegisterSetting
    public final BooleanSetting armor = new BooleanSetting("Armor", true);
    @RegisterSetting
    public final BooleanSetting surroundBlocks = new BooleanSetting("SurroundBlocks", true);
    @RegisterSetting
    public final BooleanSetting hands = new BooleanSetting("OffhandMainhand", true);

    public String nameString;

    @Override
    public String getMetaData() {
        if(nameString != null) {
            return " [" + ChatFormatting.GRAY + nameString + ChatFormatting.RESET + "]";
        }else {
            return "";
        }
    }

    @SubscribeEvent
    public void onUpdate(RenderGameOverlayEvent.Text event) {
        EntityPlayer entityPlayer;
        if(autoCrystalTarget.isEnabled()) {
            entityPlayer = notorious.hackManager.getHack(AutoCrystal.class).targetPlayer;
        }else {
            entityPlayer = (EntityPlayer) mc.world.loadedEntityList.stream()
                    .filter(entity -> entity instanceof EntityPlayer)
                    .filter(entity -> entity != mc.player)
                    .map(entity -> (EntityLivingBase) entity)
                    .min(Comparator.comparing(c -> mc.player.getDistance(c)))
                    .orElse(null);
        }
        if(entityPlayer != null) {
            if(friendSkip.isEnabled() && notorious.friend.isFriend(entityPlayer.getName()))
                return;
            if(entityPlayer.getDistance(mc.player) <= range.getValue()) {
                nameString = entityPlayer.getDisplayNameString();
                ////////////////////////////////////////////////background////////////////////////////////////////////////
                if(background.isEnabled()) {
                    Gui.drawRect((int) x.getValue(), (int) y.getValue(), (int) x.getValue() + 190, (int) y.getValue() + 50, new Color(0, 0, 0, 255).getRGB());
                }
                ////////////////////////////////////////////////rainbow line////////////////////////////////////////////////
                if(rainbowLine.isEnabled()) {
                    Gui.drawRect((int) x.getValue(), (int) y.getValue(), (int) x.getValue() + 190, (int) y.getValue() + 2, ColorUtil.getRainbow(6f, 1f));
                }
                ////////////////////////////////////////////////name////////////////////////////////////////////////
                if(name.isEnabled()) {
                    mc.fontRenderer.drawStringWithShadow(entityPlayer.getName(), x.getValue() + 5, y.getValue() + 5, notorious.friend.isFriend(entityPlayer.getName()) ? new Color(0, 255, 234).getRGB() : -1);
                }
                ////////////////////////////////////////////////health////////////////////////////////////////////////
                int healthInt = (int) (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount());
                Color healthColor = null;
                if(healthInt > 19) {
                    healthColor = new Color(0, 255, 0, 255);
                }else if(healthInt > 10) {
                    healthColor = new Color(255, 255, 0, 255);
                }else if(healthInt > 0) {
                    healthColor = new Color(255, 0, 0, 255);
                }else {
                    healthColor = new Color(0, 0, 0, 255);
                }
                if(health.isEnabled()) {
                    mc.fontRenderer.drawStringWithShadow("HP:" + healthInt, x.getValue() + 5, y.getValue() + 15, healthColor.getRGB());
                }
                ////////////////////////////////////////////////player view////////////////////////////////////////////////
                if(playerView.isEnabled()) {
                    GlStateManager.color(1f, 1f, 1f);
                    GuiInventory.drawEntityOnScreen((int) x.getValue() + 115, (int) y.getValue() + 48, 20, 0, 0, entityPlayer);
                }
                ////////////////////////////////////////////////ping////////////////////////////////////////////////
                Color pingColor = null;
                if(getPing(entityPlayer) < 49) {
                    pingColor = new Color(0, 255, 0, 255);
                }else if(getPing(entityPlayer) < 99) {
                    pingColor = new Color(255, 255, 0, 255);
                }else if(getPing(entityPlayer) >= 100) {
                    pingColor = new Color(255, 0, 0, 255);
                }else {
                    pingColor = new Color(0, 0, 0, 255);
                }
                if(ping.isEnabled()) {
                    mc.fontRenderer.drawStringWithShadow("Ping:" + getPing(entityPlayer), x.getValue() + 5, y.getValue() + 25, pingColor.getRGB());
                }
                ////////////////////////////////////////////////fucked detector////////////////////////////////////////////////
                String fuckedDetector = "";
                Color fuckedColor;
                if(isFucked(entityPlayer)) {
                    fuckedDetector = "FUCKED";
                    fuckedColor = new Color(0, 255, 0, 255);
                }else if(!isFucked(entityPlayer)) {
                    fuckedDetector = "SAFE";
                    fuckedColor = new Color(255, 0, 0, 255);
                }else {
                    fuckedColor = new Color(0, 0, 0, 255);
                }
                if(fucked.isEnabled()) {
                    mc.fontRenderer.drawStringWithShadow(fuckedDetector, x.getValue() + 5, y.getValue() + 35, fuckedColor.getRGB());
                }
                ////////////////////////////////////////////////totem pop counter////////////////////////////////////////////////
                final ItemStack itemStack = new ItemStack(Items.TOTEM_OF_UNDYING);
                renderItem(itemStack, (int) x.getValue() + 142, (int) y.getValue() + 26);
                String pops = "0";
                if (notorious.popListener.popMap.get(entityPlayer.getName()) != null)
                    pops = notorious.popListener.popMap.get(entityPlayer.getName()).toString();
                if(totemPopCounter.isEnabled())
                    mc.fontRenderer.drawStringWithShadow(pops, x.getValue() + 158, y.getValue() + 32, -1);
                ////////////////////////////////////////////////armor////////////////////////////////////////////////
                if(armor.isEnabled()) {
                    int yOffset = 10;
                    for (ItemStack stack : entityPlayer.getArmorInventoryList()) {
                        if (stack == null) continue;
                        ItemStack armourStack = stack.copy();
                        renderItem(armourStack, (int) x.getValue() + 125, yOffset + (int) y.getValue() + 26);
                        yOffset -= 12;
                    }
                }
                ////////////////////////////////////////////////surround blocks////////////////////////////////////////////////
                if(surroundBlocks.isEnabled()) {
                    ArrayList<Block> surroundblocks = getSurroundBlocks(entityPlayer);
                    renderItem(new ItemStack(surroundblocks.get(0)), (int) x.getValue() + 73, (int) y.getValue() + 10);
                    renderItem(new ItemStack(surroundblocks.get(1)), (int) x.getValue() + 61, (int) y.getValue() + 21);
                    renderItem(new ItemStack(surroundblocks.get(2)), (int) x.getValue() + 73, (int) y.getValue() + 32);
                    renderItem(new ItemStack(surroundblocks.get(3)), (int) x.getValue() + 85, (int) y.getValue() + 21);
                }
                ////////////////////////////////////////////////mainhand and offhand////////////////////////////////////////////////
                if(hands.isEnabled()) {
                    final ItemStack mainHand = new ItemStack(entityPlayer.getHeldItemMainhand().getItem());
                    final ItemStack offHand = new ItemStack(entityPlayer.getHeldItemOffhand().getItem());
                    renderItem(mainHand, (int) x.getValue() + 160, (int) y.getValue() + 5);
                    renderItem(offHand, (int) x.getValue() + 142, (int) y.getValue() + 5);
                }
            }
        }
    }

    //skidded from wp2
    public boolean isFucked(EntityPlayer player) {
        BlockPos pos = new BlockPos(player.posX, player.posY - 1, player.posZ);
        if (canPlaceCrystal(pos.south()) || (canPlaceCrystal(pos.south().south()) && mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR)) {
            return true;
        }
        if (canPlaceCrystal(pos.east()) || (canPlaceCrystal(pos.east().east()) && mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR)) {
            return true;
        }
        if (canPlaceCrystal(pos.west()) || (canPlaceCrystal(pos.west().west()) && mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR)) {
            return true;
        }
        if (canPlaceCrystal(pos.north()) || (canPlaceCrystal(pos.north().north()) && mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR)) {
            return true;
        }
        return false;
    }

    //skidded from wp2
    public boolean canPlaceCrystal(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
            Block floor = mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            Block ceil = mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();
            if (floor == Blocks.AIR && ceil == Blocks.AIR && mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(pos.add(0, 1, 0))).isEmpty() && mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(pos.add(0, 2, 0))).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public int getPing(EntityPlayer player) {
        int ping = 0;
        try {
            ping = (int) MathUtil.clamp(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime(), 1, 99999);
        }
        catch (NullPointerException ignored) {
        }
        return ping;
    }

    private void renderItem(ItemStack itemStack, int posX, int posY) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        mc.getRenderItem().zLevel = -150.0f;
        RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, posX, posY);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, itemStack, posX, posY);
        RenderHelper.disableStandardItemLighting();
        mc.getRenderItem().zLevel = 0.0f;
    }

    //skidded from https://github.com/SomePineaple/PineapleClient/blob/c736e94775a953b069c45fd0a991c64b792a0612/src/main/java/me/somepineaple/pineapleclient/main/guiscreen/hud/EnemyInfo.java#L129
    private ArrayList<Block> getSurroundBlocks(EntityLivingBase e) {
        ArrayList<Block> surroundblocks = new ArrayList<>();
        BlockPos entityblock = new BlockPos(Math.floor(e.posX), Math.floor(e.posY), Math.floor(e.posZ));
        surroundblocks.add(mc.world.getBlockState(entityblock.north()).getBlock());
        surroundblocks.add(mc.world.getBlockState(entityblock.east()).getBlock());
        surroundblocks.add(mc.world.getBlockState(entityblock.south()).getBlock());
        surroundblocks.add(mc.world.getBlockState(entityblock.west()).getBlock());
        return surroundblocks;
    }
}
