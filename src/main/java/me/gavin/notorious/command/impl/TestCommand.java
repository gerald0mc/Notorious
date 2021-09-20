package me.gavin.notorious.command.impl;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.command.Command;
import me.gavin.notorious.command.RegisterCommand;

@RegisterCommand(name = "Test", description = "Just a test command.", syntax = "test <test>", aliases = {"t", "te", "st"})
public class TestCommand extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        if (args.length == 1){
            if (args[0].equalsIgnoreCase("one")){
                Notorious.INSTANCE.messageManager.sendMessage(getName());
            } else if (args[0].equalsIgnoreCase("two")){
                Notorious.INSTANCE.messageManager.sendMessage(getDescription());
            } else if (args[0].equalsIgnoreCase("three")){
                Notorious.INSTANCE.messageManager.sendMessage(getSyntax() + " This is not an actual syntax message!");
            } else {
                sendSyntax();
            }
        }
    }
}