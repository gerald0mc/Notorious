package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterValue;
import me.gavin.notorious.setting.Value;
import me.gavin.notorious.setting.ValueGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
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

@RegisterHack(name = "KillAura", description = "Attacks entities for you", category = Hack.Category.Combat, bind = Keyboard.KEY_R)
public class KillAura extends Hack {

    // TODO: Add rotations

    public final ValueGroup targets = new ValueGroup("Targets");

    /* Targets  */
    @RegisterValue
    public final Value<Boolean> players = new Value<>("Players", true).withGroup(targets);
    @RegisterValue
    public final Value<Boolean> animals = new Value<>("Animals", false).withGroup(targets);
    @RegisterValue
    public final Value<Boolean> mobs = new Value<>("Mobs", false).withGroup(targets);
    @RegisterValue
    public final Value<Boolean> other = new Value<>("Other", false).withGroup(targets);

    @RegisterValue
    public final Value<Boolean> attackDelay = new Value<>("1.9 Delay", true);

    @RegisterValue
    public final Value<Float> range = new Value<>("Range", 4f, 1f, 5f);

    @RegisterValue
    public final Value<Integer> attackSpeed = new Value<>("AttackSpeed", 10, 2, 18);

    @SubscribeEvent
    public void onLivingUpdate(PlayerLivingUpdateEvent event) {
        for (Entity entity : mc.world.loadedEntityList) {
            if(entity.equals(mc.player))
                continue;
            if(entity instanceof EntityPlayer && players.value) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                if(ent.getDistance(mc.player) <= range.value && ent.getHealth() > 0) {
                    if(attackDelay.value) {
                        if(mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                            mc.playerController.attackEntity(mc.player, ent);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    }else {
                        if(mc.player.ticksExisted % attackSpeed.value == 0.0) {
                            mc.playerController.attackEntity(mc.player, ent);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    }
                }
            }
            if(entity instanceof EntityAnimal && animals.value) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                if(ent.getDistance(mc.player) <= range.value && ent.getHealth() > 0) {
                    if(attackDelay.value) {
                        if(mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                            mc.playerController.attackEntity(mc.player, ent);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    }else {
                        if(mc.player.ticksExisted % attackSpeed.value == 0.0) {
                            mc.playerController.attackEntity(mc.player, ent);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    }
                }
            }
            if(entity instanceof IMob && mobs.value) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                if(ent.getDistance(mc.player) <= range.value && ent.getHealth() > 0) {
                    if(attackDelay.value) {
                        if(mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                            mc.playerController.attackEntity(mc.player, ent);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    }else {
                        if(mc.player.ticksExisted % attackSpeed.value == 0.0) {
                            mc.playerController.attackEntity(mc.player, ent);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    }
                }
            }
        }
    }
}