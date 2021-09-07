package dev.notorious.client.managers;

import com.google.common.reflect.ClassPath;
import com.google.gson.*;
import dev.notorious.client.commands.Command;
import dev.notorious.client.commands.RegisterCommand;
import dev.notorious.client.util.ChatUtils;
import dev.notorious.client.util.IMinecraft;
import dev.notorious.client.value.Value;
import dev.notorious.client.value.impl.*;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                    ChatUtils.sendChatMessage("Command could not be found.");
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

    public void loadConfig() throws IOException {
        if (!Files.exists(Paths.get("Notorious/Client/Prefix.json"))) return;

        InputStream stream = Files.newInputStream(Paths.get("Notorious/Client/Prefix.json"));
        JsonObject prefixObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
        if (prefixObject.get("Prefix") == null) return;

        JsonElement dataObject = prefixObject.get("Prefix");
        if (dataObject != null && dataObject.isJsonPrimitive()){
            setPrefix(dataObject.getAsString());
        }

        stream.close();
    }

    public void saveConfig() throws IOException {
        if (Files.exists(Paths.get("Notorious/Client/Prefix.json")))
            new File("Notorious/Client/Prefix.json").delete();
        Files.createFile(Paths.get("Notorious/Client/Prefix.json"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream("Notorious/Client/Prefix.json"), StandardCharsets.UTF_8);
        JsonObject prefixObject = new JsonObject();

        prefixObject.add("Prefix", new JsonPrimitive(getPrefix()));

        stream.write(gson.toJson(new JsonParser().parse(prefixObject.toString())));
        stream.close();
    }
}
