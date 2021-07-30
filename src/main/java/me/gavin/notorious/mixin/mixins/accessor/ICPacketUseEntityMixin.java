package me.gavin.notorious.mixin.mixins.accessor;

import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketUseEntity.class)
public interface ICPacketUseEntityMixin {

    @Accessor("entityId")
    void setEntityIdAccessor(int id);

    @Accessor("action")
    void setActionAccessor(CPacketUseEntity.Action action);

    @Accessor("hand")
    void setHandAccessor(EnumHand hand);
}
