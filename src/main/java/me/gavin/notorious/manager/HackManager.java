package me.gavin.notorious.manager;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.Setting;
import org.reflections.Reflections;

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

        try {
            loadHacks();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

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

    private void loadHacks() throws IllegalAccessException, InstantiationException {
        final Reflections reflections = new Reflections("me.gavin.notorious.hack");

        for (Class<? extends Hack> clazz : reflections.getSubTypesOf(Hack.class)) {
            if (clazz.isAnnotationPresent(RegisterHack.class)) {
                final RegisterHack registerAnnotation = clazz.getAnnotation(RegisterHack.class);

                final Hack hack = clazz.newInstance();
                hack.setName(registerAnnotation.name());
                hack.setDescription(registerAnnotation.description());
                hack.setCategory(registerAnnotation.category());
                hack.setBind(registerAnnotation.bind());

                for (Field field : clazz.getDeclaredFields()) {
                    if (Setting.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(RegisterSetting.class)) {
                        hack.getSettings().add((Setting) field.get(hack));
                    }
                }

                hacks.add(hack);
            }
        }
    }

    private int sortABC(Hack hack1, Hack hack2) {
        return hack1.getName().compareTo(hack2.getName());
    }
}