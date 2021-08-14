package me.gavin.notorious.setting;

public class StringSetting extends Setting{
    private String string;

    public StringSetting(String name, String typeable) {
        super(name);
        this.string = typeable;
    }

    public String getString() {
        return string;
    }

    public void setString(String set) {
        set = string;
    }
}
