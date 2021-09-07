package dev.notorious.client.managers;

import com.google.common.reflect.ClassPath;
import dev.notorious.client.event.impl.DeathEvent;
import dev.notorious.client.event.impl.LivingUpdateEvent;
import dev.notorious.client.event.impl.TextRenderEvent;
import dev.notorious.client.event.impl.WorldRenderEvent;
import dev.notorious.client.hacks.Hack;
import dev.notorious.client.hacks.RegisterHack;
import dev.notorious.client.util.IMinecraft;
import dev.notorious.client.value.Value;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HackManager implements IMinecraft {
    private final ArrayList<Hack> hacks;

    public HackManager(){
        MinecraftForge.EVENT_BUS.register(this);
        hacks = new ArrayList<>();

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            final ClassPath path = ClassPath.from(loader);
            for (ClassPath.ClassInfo info : path.getTopLevelClassesRecursive("dev.notorious.client.hacks")){
                final Class<?> hackClass = info.load();
                if (Hack.class.isAssignableFrom(hackClass)){
                    if (hackClass.isAnnotationPresent(RegisterHack.class)){
                        register((Hack) hackClass.newInstance());
                    }
                }
            }
        } catch (IOException | IllegalAccessException | InstantiationException exception){
            exception.printStackTrace();
        }
    }

    public void register(Hack hack){
        try {
            for (Field field : hack.getClass().getDeclaredFields()){
                if (Value.class.isAssignableFrom(field.getType())){
                    if (!field.isAccessible()) field.setAccessible(true);
                    hack.register((Value) field.get(hack));
                }
            }

            hack.register(hack.displayName);
            hack.register(hack.chatNotify);
            hack.register(hack.drawn);
            hack.register(hack.bind);
            hacks.add(hack);
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public ArrayList<Hack> getHacks(){
        return hacks;
    }

    public ArrayList<Hack> getHacks(Hack.Category category){
        return (ArrayList<Hack>) hacks.stream().filter(mm -> mm.getCategory().equals(category)).collect(Collectors.toList());
    }

    public boolean isHackEnabled(String name){
        Hack hack = hacks.stream().filter(mm -> mm.getName().equals(name)).findFirst().orElse(null);
        if (hack != null){
            return hack.isToggled();
        }else{
            return false;
        }
    }

    public void disableHack(String name, boolean message){
        hacks.stream().filter(mm -> mm.getName().equals(name)).findFirst().ifPresent(mm -> mm.disable(message));
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if (mc.player != null && mc.world != null){
            hacks.stream().filter(Hack::isToggled).forEach(Hack::onTick);
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingUpdateEvent event){
        if (mc.player != null && mc.world != null){
            hacks.stream().filter(Hack::isToggled).forEach(Hack::onUpdate);
        }
    }

    @SubscribeEvent
    public void onTextRender(RenderGameOverlayEvent.Post event){
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT){
            TextRenderEvent renderEvent = new TextRenderEvent(event.getPartialTicks());
            hacks.stream().filter(Hack::isToggled).forEach(mm -> mm.onRender(renderEvent));
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) return;

        mc.profiler.startSection("notorious");
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL32.GL_DEPTH_CLAMP);

        WorldRenderEvent renderEvent = new WorldRenderEvent(event.getPartialTicks());
        mc.profiler.endSection();
        hacks.stream().filter(Hack::isToggled).forEach(mm -> mm.onRender(renderEvent));

        GL11.glDisable(GL32.GL_DEPTH_CLAMP);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        mc.profiler.endSection();
    }

    @SubscribeEvent
    public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent event){
        hacks.stream().filter(Hack::isToggled).forEach(Hack::onLogin);
    }

    @SubscribeEvent
    public void onLogout(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
        hacks.stream().filter(Hack::isToggled).forEach(Hack::onLogout);
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event){
        hacks.stream().filter(Hack::isToggled).forEach(Hack::onDeath);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event){
        if (Keyboard.getEventKeyState()){
            if (Keyboard.getEventKey() == Keyboard.KEY_NONE) return;
            for (Hack hack : hacks){
                if (hack.getBind() == Keyboard.getEventKey()){
                    hack.toggle(true);
                }
            }
        }
    }

    public void loadConfig() throws IOException{
        for (Hack hack : hacks){
            hack.loadConfig();
        }
    }

    public void saveConfig() throws IOException{
        for (Hack hack : hacks){
            hack.saveConfig();
        }
    }
}