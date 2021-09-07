package dev.notorious.client.value;

public class Value {
    private final String name;
    private final String displayName;
    private final String description;

    public Value(final String name){
        this.name = name;
        this.displayName = name;
        this.description = "No description.";
    }

    public Value(final String name, final String displayName){
        this.name = name;
        this.displayName = displayName;
        this.description = "No description.";
    }

    public Value(final String name, final String displayName, final String description){
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}