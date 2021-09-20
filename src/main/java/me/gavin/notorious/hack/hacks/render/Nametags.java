package me.gavin.notorious.hack.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.ColorUtil;
import me.gavin.notorious.util.ProjectionUtil;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@RegisterHack(name = "Nametags", description = "ez", category = Hack.Category.Render)
public class Nametags extends Hack {

    @RegisterSetting public final NumSetting scale = new NumSetting("Scale", 2.5f, 1, 5, 0.1f);
    @RegisterSetting public final ColorSetting outlineColor = new ColorSetting("OutlineColor", 120, 81, 169, 255);
    @RegisterSetting public final BooleanSetting rainbow = new BooleanSetting("Rainbow", true);
    @RegisterSetting public final BooleanSetting armor = new BooleanSetting("Armor", false);
    @RegisterSetting public final BooleanSetting items = new BooleanSetting("Items", true);

    @SubscribeEvent
    public void onRender2d(RenderGameOverlayEvent.Text event) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.equals(mc.player))
                continue;

            GlStateManager.pushMatrix();
            double yAdd = player.isSneaking() ? 1.75 : 2.25;

            double deltaX = MathHelper.clampedLerp(player.lastTickPosX, player.posX, event.getPartialTicks());
            double deltaY = MathHelper.clampedLerp(player.lastTickPosY, player.posY, event.getPartialTicks());
            double deltaZ = MathHelper.clampedLerp(player.lastTickPosZ, player.posZ, event.getPartialTicks());


            Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, yAdd, 0));

            GlStateManager.translate(projection.x, projection.y, 0);
            GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
            int ping = -1;

            if (mc.getConnection() != null) {
                ping = mc.getConnection().getPlayerInfo(player.getUniqueID()).getResponseTime();
            }

            double health = player.getHealth() + player.getAbsorptionAmount();

            //le render
            String str = "";
            str += ChatFormatting.GRAY + "" + ping + "ms " + ChatFormatting.RESET;
            str += player.getName();
            str += " " + getHealthColor(health) + String.format("%.1f", health);
            if(armor.isEnabled()) {
                int xOffset = 1;
                for (ItemStack stack : player.getArmorInventoryList()) {
                    if (stack == null) continue;
                    ItemStack armourStack = stack.copy();
                    renderItem(armourStack, xOffset + 10, -(26));
                    xOffset -= 13;
                }
            }
            mc.player.getName();
            RenderUtil.drawBorderedRect(-((mc.fontRenderer.getStringWidth(str) + 2) / 2f), -(mc.fontRenderer.FONT_HEIGHT + 2), (float) ((mc.fontRenderer.getStringWidth(str) + 2) / (int) 2f), 1, new Color(0, 0, 0, 125).getRGB(), rainbow.getValue() ? ColorUtil.getRainbow(6f, 1f) : outlineColor.getAsColor().getRGB());

            mc.fontRenderer.drawStringWithShadow(str, -(mc.fontRenderer.getStringWidth(str) / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
            GlStateManager.popMatrix();
        }
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

    private ChatFormatting getHealthColor(double health) {
        if (health >= 15.0) {
            return ChatFormatting.GREEN;
        } else if (health >= 10.0) {
            return ChatFormatting.YELLOW;
        } else if (health >= 5.0) {
            return ChatFormatting.RED;
        } else {
            return ChatFormatting.DARK_RED;
        }
    }
}
