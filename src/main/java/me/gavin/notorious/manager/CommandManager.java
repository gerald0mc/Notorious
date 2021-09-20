package me.gavin.notorious.manager;

import com.google.common.reflect.ClassPath;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.command.Command;
import me.gavin.notorious.command.RegisterCommand;
import me.gavin.notorious.stuff.IMinecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager implements IMinecraft {
    private String prefix = ".";
    private final ArrayList<Command> commands;

    public CommandManager(){
        MinecraftForge.EVENT_BUS.register(this);
        commands = new ArrayList<>();

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            final ClassPath path = ClassPath.from(loader);
            for (ClassPath.ClassInfo info : path.getTopLevelClassesRecursive("dev.notorious.client.commands.impl")){
                final Class<?> commandClass = info.load();
                if (Command.class.isAssignableFrom(commandClass)){
                    if (commandClass.isAnnotationPresent(RegisterCommand.class)){
                        commands.add((Command) commandClass.newInstance());
                    }
                }
            }
        } catch (IOException | IllegalAccessException | InstantiationException exception){
            exception.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onChatSent(ClientChatEvent event){
        String message = event.getMessage();

        if (message.startsWith(getPrefix())) {
            event.setCanceled(true);
            message = message.substring(getPrefix().length());

            if (message.split(" ").length > 0){
                String name = message.split(" ")[0];
                boolean found = false;

                for (Command command : getCommands()){
                    if (command.getAliases().contains(name.toLowerCase()) || command.getName().equalsIgnoreCase(name)){
                        mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                        command.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
                        found = true;
                        break;
                    }
                }

                if (!found){
                    Notorious.INSTANCE.messageManager.sendError("Command could not be found.");
                }
            }
        }
    }

    public ArrayList<Command> getCommands(){
        return commands;
    }

    public String getPrefix(){
        return prefix;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }
}
