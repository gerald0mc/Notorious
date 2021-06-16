package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterValue;
import me.gavin.notorious.setting.Value;
import me.gavin.notorious.setting.ValueGroup;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "KillAura", description = "Attacks entities for you", category = Hack.Category.Combat)
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

        }
    }
}
