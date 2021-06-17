package me.gavin.notorious.mixin.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.gavin.notorious.event.events.PacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class NetworkManagerMixin {

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void sendPacketInject(Packet<?> packetIn, CallbackInfo ci) {
        final PacketEvent.Send event = new PacketEvent.Send(packetIn);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
            ci.cancel();
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    public void channelRead0Inject(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo ci) {
        final PacketEvent.Receive event = new PacketEvent.Receive(p_channelRead0_2_);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
            ci.cancel();
    }
}
