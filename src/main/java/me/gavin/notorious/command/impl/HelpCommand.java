package me.gavin.notorious.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.command.Command;
import me.gavin.notorious.command.RegisterCommand;

@RegisterCommand(name = "Help", description = "Tells you stuff about the client and client stuff.", syntax = "help", aliases = {"h", "he", "lp"})
public class HelpCommand extends Command {
    @Override
    public void onCommand(String[] args, String command) {
        Notorious.INSTANCE.messageManager.sendMessage("Notorious Help:");
        Notorious.INSTANCE.messageManager.sendMessage("TestCommand - " + ChatFormatting.GRAY + "Just a test command.");
    }
}
