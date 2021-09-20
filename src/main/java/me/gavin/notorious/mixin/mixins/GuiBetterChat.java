package me.gavin.notorious.mixin.mixins;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.hacks.chat.ChatMods;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({GuiNewChat.class})
public class GuiBetterChat {

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V", ordinal = 0))
    private void overrideChatBackgroundColour(int left, int top, int right, int bottom, int color) {
        if(!Notorious.INSTANCE.hackManager.getHack(ChatMods.class).clearChat.isEnabled() && Notorious.INSTANCE.hackManager.getHack(ChatMods.class).isEnabled() || !Notorious.INSTANCE.hackManager.getHack(ChatMods.class).isEnabled()) {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }
}
