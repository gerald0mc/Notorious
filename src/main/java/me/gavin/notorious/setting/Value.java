package me.gavin.notorious.setting;

/**
 * @author Gav06
 * @since 6/15/2021
 */

public class Value<T> {

    public String name;

    public T value;
    public T min;
    public T max;

    public ValueGroup group;

    public Value(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public Value(String name, T value, T min, T max) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public T clamp(T value, T min, T max) {
        return ((Comparable) value).compareTo(min) < 0 ? min : (((Comparable) value).compareTo(max) > 0 ? max : value);
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getMeta() {
        if (value != null)
            return value.toString();
        else
            return name;
    }

    public Value<T> withGroup(ValueGroup group) {
        group.getValues().add(this);
        this.group = group;
        return this;
    }
}