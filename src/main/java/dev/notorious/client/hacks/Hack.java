package dev.notorious.client.hacks;

import com.google.gson.*;
import dev.notorious.client.event.impl.TextRenderEvent;
import dev.notorious.client.event.impl.WorldRenderEvent;
import dev.notorious.client.value.Value;
import dev.notorious.client.value.impl.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Hack {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Value> values;

    private final String name;
    private final String description;
    private final Category category;

    private boolean toggled;
    private final boolean persistent;
    private final boolean hidden;

    public final ValueString displayName = new ValueString("DisplayName", "Display Name", "The module's display name.", "Placeholder");
    public final ValueBoolean chatNotify = new ValueBoolean("ChatNotify", "Chat Notify", "Sends a message when the module is toggled.", false);
    public final ValueBoolean drawn = new ValueBoolean("Drawn", "Drawn", "Draws the module on the Array List.", true);
    public final ValueBind bind = new ValueBind("Bind", "Bind", "The module's toggle bind.", Keyboard.KEY_NONE);

    public Hack(){
        final RegisterHack annotation = getClass().getAnnotation(RegisterHack.class);

        this.name = annotation.name();
        this.displayName.setValue(annotation.displayName());
        this.description = annotation.description();
        this.category = annotation.category();

        this.persistent = annotation.persistent();
        this.hidden = annotation.hidden();
        this.bind.setValue(annotation.bind());
        this.values = new ArrayList<>();
    }

    public void onTick() {}
    public void onUpdate() {}
    public void onRender(TextRenderEvent event) {}
    public void onRender(WorldRenderEvent event) {}
    public void onEnable() {}
    public void onDisable() {}
    public void onLogin() {}
    public void onLogout() {}
    public void onDeath() {}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getDisplayName() {
        return displayName.getValue();
    }

    public void setDisplayName(String displayName) {
        this.displayName.setValue(displayName);
    }

    public boolean isChatNotify() {
        return chatNotify.getValue();
    }

    public void setChatNotify(boolean chatNotify) {
        this.chatNotify.setValue(chatNotify);
    }

    public boolean isDrawn() {
        return drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public int getBind() {
        return bind.getValue();
    }

    public void setBind(int bind) {
        this.bind.setValue(bind);
    }

    public boolean isToggled(){
        return toggled;
    }

    public void setToggled(boolean toggled){
        this.toggled = toggled;
    }

    public void persist(){
        if (persistent){
            setToggled(true);
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void toggle(boolean message){
        if (!persistent) {
            if (isToggled()) {
                disable(message);
            } else {
                enable(message);
            }
        }
    }

    public void enable(boolean message){
        if (!persistent){
            setToggled(true);
            onEnable();

            MinecraftForge.EVENT_BUS.register(this);
            if (message); // do toggle message here
        }
    }

    public void disable(boolean message){
        if (!persistent){
            setToggled(false);
            onDisable();

            MinecraftForge.EVENT_BUS.register(this);
            if (message); // do toggle message here
        }
    }

    public void register(final Value value){
        values.add(value);
    }

    public ArrayList<Value> getValues(){
        return values;
    }

    public void loadConfig() throws IOException {
        if (!Files.exists(Paths.get("Notorious/Hacks/" + category.getName() + "/" + getName() + ".json"))) return;

        InputStream stream = Files.newInputStream(Paths.get("Notorious/Hacks/" + category.getName() + "/" + getName() + ".json"));
        JsonObject hackObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
        if (hackObject.get("Name") == null) return;

        JsonObject toggleObject = hackObject.get("Toggle").getAsJsonObject();
        JsonElement toggleDataObject = toggleObject.get("Toggle");
        if (toggleDataObject != null && toggleDataObject.isJsonPrimitive()){
            if (toggleDataObject.getAsBoolean()){
                enable(false);
            }
        }

        JsonObject valueObject = hackObject.get("Values").getAsJsonObject();
        for (Value value : values){
            JsonElement dataObject = valueObject.get(value.getName());
            if (dataObject != null && dataObject.isJsonPrimitive()){
                if (value instanceof ValueBoolean){
                    ((ValueBoolean) value).setValue(dataObject.getAsBoolean());
                } else if (value instanceof ValueInteger){
                    ((ValueInteger) value).setValue(dataObject.getAsInt());
                } else if (value instanceof ValueDouble){
                    ((ValueDouble) value).setValue(dataObject.getAsDouble());
                } else if (value instanceof ValueFloat){
                    ((ValueFloat) value).setValue(dataObject.getAsFloat());
                } else if (value instanceof ValueMode){
                    ((ValueMode) value).setValue(dataObject.getAsString());
                } else if (value instanceof ValueString){
                    ((ValueString) value).setValue(dataObject.getAsString());
                } else if (value instanceof ValueColor){
                    ((ValueColor) value).setColor(new Color(dataObject.getAsInt()));
                } else if (value instanceof ValueBind){
                    ((ValueBind) value).setValue(dataObject.getAsInt());
                }
            }
        }

        stream.close();
    }

    public void saveConfig() throws IOException {
        if (Files.exists(Paths.get("Notorious/Hacks/" + category.getName() + "/" + getName() + ".json")))
            new File("Notorious/Hacks/" + category.getName() + "/" + getName() + ".json").delete();
        Files.createFile(Paths.get("Notorious/Hacks/" + category.getName() + "/" + getName() + ".json"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream("Notorious/Hacks/" + category.getName() + "/" + getName() + ".json"), StandardCharsets.UTF_8);
        JsonObject hackObject = new JsonObject();
        JsonObject valueObject = new JsonObject();
        hackObject.add("Name", new JsonPrimitive(getName()));
        hackObject.add("Toggle", new JsonPrimitive(isToggled()));

        for (Value value : values){
            if (value instanceof ValueBoolean){
                valueObject.add(value.getName(), new JsonPrimitive(((ValueBoolean) value).getValue()));
            } else if (value instanceof ValueInteger){
                valueObject.add(value.getName(), new JsonPrimitive(((ValueInteger) value).getValue()));
            } else if (value instanceof ValueDouble){
                valueObject.add(value.getName(), new JsonPrimitive(((ValueDouble) value).getValue()));
            } else if (value instanceof ValueFloat){
                valueObject.add(value.getName(), new JsonPrimitive(((ValueFloat) value).getValue()));
            } else if (value instanceof ValueMode){
                valueObject.add(value.getName(), new JsonPrimitive(((ValueMode) value).getValue()));
            } else if (value instanceof ValueString){
                valueObject.add(value.getName(), new JsonPrimitive(((ValueString) value).getValue()));
            } else if (value instanceof ValueColor){
                valueObject.add(value.getName(), new JsonPrimitive(((ValueColor) value).getColor().getRGB()));
            } else if (value instanceof ValueBind){
                valueObject.add(value.getName(), new JsonPrimitive(((ValueBind) value).getValue()));
            }
        }

        hackObject.add("Values", valueObject);
        stream.write(gson.toJson(new JsonParser().parse(hackObject.toString())));
        stream.close();
    }

    public enum Category {
        COMBAT("Combat"),
        PLAYER("Player"),
        MOVEMENT("Movement"),
        VISUALS("Visuals"),
        WORLD("World"),
        CLIENT("Client");

        private final String name;

        Category(final String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }
}
