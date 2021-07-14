package me.gavin.notorious.manager;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.hack.hacks.chat.*;
import me.gavin.notorious.hack.hacks.client.*;
import me.gavin.notorious.hack.hacks.combat.*;
import me.gavin.notorious.hack.hacks.misc.*;
import me.gavin.notorious.hack.hacks.movement.*;
import me.gavin.notorious.hack.hacks.player.*;
import me.gavin.notorious.hack.hacks.render.*;
import me.gavin.notorious.hack.hacks.world.*;
import me.gavin.notorious.setting.Setting;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author Gav06
 * @since 6/15/2021
 */

public class HackManager {

    private final ArrayList<Hack> hacks;
    private final ArrayList<Hack> sortedHacks;

    public HackManager() {
        hacks = new ArrayList<>();
        sortedHacks = new ArrayList<>();

        // chat
        addHack(new AutoSuicide());
        addHack(new ChatModifications());
        addHack(new PotionAlert());
        addHack(new VisualRange());

        // client
        addHack(new ClickGUI());
        addHack(new DiscordRPC());
        addHack(new Font());
        addHack(new HackList());
        addHack(new WaterMark());

        // combat
        addHack(new AnvilBurrow());
        addHack(new AutoCrystal());
        addHack(new KillAura());
        addHack(new Quiver());
        addHack(new SmartOffhand());

        // misc
        addHack(new AutoLog());
        addHack(new AutoRespawn());
        addHack(new CopyIP());
        addHack(new FakePlayer());
        addHack(new GhastNotifier());
        addHack(new WeaknessLog());

        // movement
        addHack(new AntiVoid());
        addHack(new Sprint());
        addHack(new Step());
        addHack(new Strafe());
        addHack(new Velocity());

        // player
        addHack(new ChestStealer());
        addHack(new FastPlace());

        // render
        addHack(new AntiFog());
        addHack(new BlockHighlight());
        addHack(new BreakESP());
        addHack(new ESP());
        addHack(new Fullbright());
        addHack(new HellenKeller());
        addHack(new PenisESP());
        addHack(new StorageESP());
        addHack(new ViewModel());
        addHack(new VoidESP());
        addHack(new Weather());

        // world
        addHack(new BedFucker());
        addHack(new Lawnmower());
        addHack(new ShulkerJew());

        hacks.sort(this::sortABC);
        sortedHacks.addAll(hacks);
    }

    public ArrayList<Hack> getHacks() {
        return hacks;
    }

    public ArrayList<Hack> getSortedHacks() { return sortedHacks; }

    public Hack getHack(Class<? extends Hack> clazz) {
        for (Hack hack : hacks) {
            if (hack.getClass() == clazz)
                return hack;
        }

        return null;
    }

    public ArrayList<Hack> getHacksFromCategory(Hack.Category category) {
        final ArrayList<Hack> tempList = new ArrayList<>();

        for (Hack hack : hacks) {
            if (hack.getCategory() == category)
                tempList.add(hack);
        }

        return tempList;
    }

    private void addHack(Hack hack) {
        if (!hack.getClass().isAnnotationPresent(RegisterHack.class))
            return;

        final RegisterHack annotation = hack.getClass().getAnnotation(RegisterHack.class);
        hack.setName(annotation.name());
        hack.setDescription(annotation.description());
        hack.setCategory(annotation.category());
        hack.setBind(hack.getClass() == ClickGUI.class ? Keyboard.KEY_U : Keyboard.KEY_NONE);
        for (Field field : hack.getClass().getDeclaredFields()) {
            if (!field.isAccessible())
                field.setAccessible(true);

            if (Setting.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(RegisterSetting.class)) {
                try {
                    hack.getSettings().add((Setting) field.get(hack));
                } catch (Exception e) { e.printStackTrace(); }
            }
        }

        hacks.add(hack);
    }

    private int sortABC(Hack hack1, Hack hack2) {
        return hack1.getName().compareTo(hack2.getName());
    }
}