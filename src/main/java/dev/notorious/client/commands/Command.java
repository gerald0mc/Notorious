package dev.notorious.client.commands;

import dev.notorious.client.util.ChatUtils;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.List;

public abstract class Command {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    private final String name;
    private final String description;
    private final String syntax;
    private final List<String> aliases;

    public Command(){
        final RegisterCommand annotation = getClass().getAnnotation(RegisterCommand.class);

        this.name = annotation.name();
        this.description = annotation.description();
        this.syntax = annotation.syntax();
        this.aliases = Arrays.asList(annotation.aliases());
    }

    public abstract void onCommand(String[] args, String command);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSyntax() {
        return syntax;
    }

    public void sendSyntax(){
        ChatUtils.sendChatMessage(getSyntax());
    }

    public List<String> getAliases() {
        return aliases;
    }
}