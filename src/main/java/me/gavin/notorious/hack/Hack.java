package me.gavin.notorious.hack;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.Toggleable;
import me.gavin.notorious.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

/**
 * @author Gav06
 * @since 6/15/2021
 */

public abstract class Hack implements Toggleable {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final Notorious notorious = Notorious.INSTANCE;

    private String name;
    private String description;
    private Category category;

    private int keybind;

    private boolean enabled;

    private final ArrayList<Setting> settings = new ArrayList<>();

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        if (enabled) disable(); else enable();
    }

    public void enable() {
        enabled = true;
        MinecraftForge.EVENT_BUS.register(this);
        onEnable();
    }

    public void disable() {
        enabled = false;
        MinecraftForge.EVENT_BUS.unregister(this);
        onDisable();
    }

    protected void onEnable() { }

    protected void onDisable() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public enum Category {
        Chat,
        Client,
        Combat,
        Misc,
        Movement,
        Player,
        Render,
        World
    }
}
