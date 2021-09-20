package me.gavin.notorious.manager;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.hack.hacks.chat.*;
import me.gavin.notorious.hack.hacks.client.*;
import me.gavin.notorious.hack.hacks.combat.*;
import me.gavin.notorious.hack.hacks.combat.AutoCrystal;
import me.gavin.notorious.hack.hacks.combat.Criticals;
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
    private ArrayList<Hack> sortedHacks;

    public HackManager() {
        hacks = new ArrayList<>();
        sortedHacks = new ArrayList<>();

        // chat
        addHack(new ArmorNotify());
        addHack(new AutoChad());
        addHack(new AutoDox());
        addHack(new AutoEZ());
        addHack(new AutoGroom());
        addHack(new ChatMods());
        addHack(new ChorusPredict());
        addHack(new PotionAlert());
        //addHack(new StringTestModule());
        addHack(new ToggleMessage());
        addHack(new PopCounter());
        addHack(new VisualRange());

        // client
        addHack(new ClickGUI());
        addHack(new DiscordRPC());
        addHack(new Font());
        addHack(new HUD());
        addHack(new Notification());
        addHack(new Save());

        // combat
        addHack(new AnvilBurrow());
        addHack(new AutoCrystal());
        addHack(new Burrow());
        addHack(new BurrowBreaker());
        addHack(new Criticals());
        //addHack(new CrystalAura());
        addHack(new KillAura());
        addHack(new PacketCity());
        addHack(new Quiver());

        // misc
        addHack(new AutoLog());
        addHack(new Respawn());
        addHack(new CopyCoords());
        addHack(new CopyIP());
        addHack(new FakePlayer());
        addHack(new GhastNotifier());
        addHack(new RBandDetect());
        addHack(new WeaknessLog());

        // movement
        addHack(new AntiVoid());
        addHack(new AutoHop());
        addHack(new ReverseStep());
        addHack(new Sprint());
        addHack(new Step());
        addHack(new Velocity());

        // player
        addHack(new FastPlace());
        addHack(new MiddleClick());
        addHack(new PacketMine());
        addHack(new Suicide());
        addHack(new ToggleXP());

        // render
        addHack(new AntiFog());
        addHack(new BlockHighlight());
        addHack(new BreakESP());
        addHack(new BurrowESP());
        addHack(new ESP());
        addHack(new FuckedDetector());
        addHack(new Fullbright());
        addHack(new Glint());
        addHack(new HellenKeller());
        addHack(new HoleESP());
        addHack(new Nametags());
        addHack(new NoRender());
        addHack(new PenisESP());
        addHack(new PopESP());
        addHack(new RBandESP());
        addHack(new ShulkerRender());
        addHack(new SkyColor());
        addHack(new Alpha());
        addHack(new StorageESP());
        addHack(new TargetHUD());
        addHack(new ViewModel());
        addHack(new VoidESP());
        addHack(new Chams());

        // world
        addHack(new BedFucker());
        addHack(new Lawnmower());
        addHack(new ShulkerJew());
        addHack(new MobOwner());

        hacks.sort(this::sortABC);
        sortedHacks.addAll(hacks);
    }

    public ArrayList<Hack> getHacks() {
        return hacks;
    }

    public ArrayList<Hack> getSortedHacks() {
        return sortedHacks;
    }

    @SuppressWarnings("unchecked")
    public <T extends Hack> T getHack(Class<T> clazz) {
        for (Hack hack : hacks) {
            if (hack.getClass() == clazz)
                return (T) hack;
        }

        return null;
    }

    public Hack getHackString(String name) {
        for (Hack h : getHacks()) {
            if(h.getName().equalsIgnoreCase(name)) {
                return h;
            }
        }
        return null;
    }

    public ArrayList<Hack> getEnabledHacksWithDrawnHacksForArraylist() {
        ArrayList<Hack> enabledHacks = new ArrayList<>();
        for (Hack hack : this.hacks) {
            if (!hack.isEnabled()) continue;
            if (!hack.isDrawn()) continue;
            enabledHacks.add(hack);
        }
        return enabledHacks;
    }

    public ArrayList<Hack> getEnabledHacks() {
        ArrayList<Hack> enabledHacks = new ArrayList<>();
        for (Hack module : this.hacks) {
            if (!module.isEnabled()) continue;
            enabledHacks.add(module);
        }
        return enabledHacks;
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