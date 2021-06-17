package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.setting.SettingGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author Gav06 and gerald0mc
 *
 * @since 6/16/2021
 */

@RegisterHack(name = "KillAura", description = "Attacks entities for you", category = Hack.Category.Combat)
public class KillAura extends Hack {

    // TODO: Add rotations

    public final SettingGroup targets = new SettingGroup("Targets");

    @RegisterSetting
    public final BooleanSetting attackDelay = new BooleanSetting("1.9 Delay", true);

    @RegisterSetting
    public final BooleanSetting players = new BooleanSetting("Players", true);
    @RegisterSetting
    public final BooleanSetting animals = new BooleanSetting("Animals", false);
    @RegisterSetting
    public final BooleanSetting mobs = new BooleanSetting("Mobs", false);

    @RegisterSetting
    public final NumSetting attackSpeed = new NumSetting("Attack Speed", 10, 2, 18, 1);
    @RegisterSetting
    public final NumSetting range = new NumSetting("Range", 4, 1, 6, 0.25f);

    public KillAura() {
        players.setGroup(targets);
        animals.setGroup(targets);
        mobs.setGroup(targets);
    }

    @SubscribeEvent
    public void onLivingUpdate(PlayerLivingUpdateEvent event) {
        for (Entity entity : mc.world.loadedEntityList) {
            if(entity.equals(mc.player))
                continue;

            if(entity instanceof EntityPlayer && players.isEnabled()) {
                attack(entity);
            }
            if(entity instanceof EntityAnimal && animals.isEnabled()) {
                attack(entity);
            }
            if(entity instanceof EntityMob && mobs.isEnabled()) {
                attack(entity);
            }
        }
    }

    private void attack(Entity entity) {
        if (shouldAttack((EntityLivingBase) entity)) {
            if (attackDelay.isEnabled()) {
                if (mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                    mc.playerController.attackEntity(mc.player, entity);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            } else {
                if (mc.player.ticksExisted % attackSpeed.getValue() == 0.0) {
                    mc.playerController.attackEntity(mc.player, entity);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }

    private boolean shouldAttack(EntityLivingBase entity) {
        return entity.getDistance(mc.player) <= range.getValue() && entity.getHealth() > 0;
    }
}