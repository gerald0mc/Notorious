package dev.notorious.client.commands.impl;

import dev.notorious.client.commands.Command;
import dev.notorious.client.commands.RegisterCommand;
import dev.notorious.client.util.ChatUtils;

@RegisterCommand(name = "Test", description = "Just a test command.", syntax = "test <test>", aliases = {"t", "te", "st"})
public class TestCommand extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        if (args.length == 1){
            if (args[0].equalsIgnoreCase("one")){
                ChatUtils.sendChatMessage(getName());
            } else if (args[0].equalsIgnoreCase("two")){
                ChatUtils.sendChatMessage(getDescription());
            } else if (args[0].equalsIgnoreCase("three")){
                ChatUtils.sendChatMessage(getSyntax() + " This is not an actual syntax message!");
            } else {
                sendSyntax();
            }
        }
    }
}
