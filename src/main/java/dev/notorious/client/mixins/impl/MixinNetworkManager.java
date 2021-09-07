package dev.notorious.client.mixins.impl;

import dev.notorious.client.event.impl.ReceivePacketEvent;
import dev.notorious.client.event.impl.SendPacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkManager.class, priority = 2147483647)
public class MixinNetworkManager {
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void preSendPacket(Packet<?> packet, CallbackInfo info){
        SendPacketEvent event = new SendPacketEvent(packet);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCancelled()){
            info.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void preReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo info){
        ReceivePacketEvent event = new ReceivePacketEvent(packet);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCancelled()){
            info.cancel();
        }
    }
}
