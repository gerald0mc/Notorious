package me.gavin.notorious.setting;

public class ModeSetting extends Setting {

    private int modeIndex;
    private String[] modes;

    public ModeSetting(String name, String value, String... modes) {
        super(name);
        this.modes = modes;
        this.modeIndex = getIndex(value);
    }

    public String getMode() {
        return modes[modeIndex];
    }

    public void setMode(String value) {
        this.modeIndex = getIndex(value);
    }

    public void cycle(boolean backwards) {
        if (!backwards) {
            if (modeIndex == modes.length - 1) {
                modeIndex = 0;
            } else {
                modeIndex++;
            }
        } else {
            if (modeIndex == 0) {
                modeIndex = modes.length - 1;
            } else {
                modeIndex++;
            }
        }
    }

    private int getIndex(String value) {
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equals(value))
                return i;
        }

        return -1;
    }
}
