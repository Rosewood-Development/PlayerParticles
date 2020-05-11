/*
 * TODO:
 * + Add ability to create/manage fixed effects from the GUI
 * * Convert fixed effect ids into names
 */

package dev.esophose.playerparticles;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.TitleAction;
import dev.esophose.playerparticles.gui.hook.PlayerChatHook;
import dev.esophose.playerparticles.hook.ParticlePlaceholderExpansion;
import dev.esophose.playerparticles.hook.PlaceholderAPIHook;
import dev.esophose.playerparticles.manager.CommandManager;
import dev.esophose.playerparticles.manager.ConfigurationManager;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.DataMigrationManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.Manager;
import dev.esophose.playerparticles.manager.ParticleGroupPresetManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.manager.ParticleStyleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.manager.PluginUpdateManager;
import dev.esophose.playerparticles.particles.listener.PPlayerCombatListener;
import dev.esophose.playerparticles.particles.listener.PPlayerMovementListener;
import dev.esophose.playerparticles.util.Metrics;
import io.netty.buffer.Unpooled;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import net.minecraft.server.v1_15_R1.PacketDataSerializer;
import net.minecraft.server.v1_15_R1.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Esophose
 */
public class PlayerParticles extends JavaPlugin {

    /**
     * The running instance of PlayerParticles on the server
     */
    private static PlayerParticles INSTANCE;

    /*
     * The plugin managers
     */
    private Map<Class<?>, Manager> managers;

    public PlayerParticles() {
        INSTANCE = this;
        this.managers = new LinkedHashMap<>();
    }

    /**
     * Executes essential tasks for starting up the plugin
     */
    @Override
    public void onEnable() {
        this.reload();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PPlayerMovementListener(), this);
        pm.registerEvents(new PPlayerCombatListener(), this);
        pm.registerEvents(new PlayerChatHook(), this);

        if (Setting.SEND_METRICS.getBoolean())
            new Metrics(this);

        if (PlaceholderAPIHook.enabled())
            new ParticlePlaceholderExpansion(this).register();

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        new BukkitRunnable() {
            private LinkedList<String> queue = new LinkedList<>();
            private String message = "Snapshot 20w17a Chat Hex Code Colors!";

            @Override
            public void run() {
                if (queue.size() >= message.length())
                    queue.poll();

                Color color = getRainbowColor();
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                queue.add(hex);

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[");
                boolean isFirst = true;
                for (int i = 0; i < queue.size(); i++) {
                    if (!isFirst)
                        stringBuilder.append(",");
                    isFirst = false;

                    stringBuilder.append("{");
                    stringBuilder.append("\"color\":\"").append(queue.get(i)).append("\",");
                    stringBuilder.append("\"text\":\"").append(message.charAt(i)).append("\"");
                    stringBuilder.append("}");
                }
                stringBuilder.append("]");

                PacketContainer timePacket = protocolManager.createPacket(PacketType.fromClass(PacketPlayOutTitle.class));
                timePacket.getTitleActions().write(0, TitleAction.TIMES);
                timePacket.getIntegers().write(0, 0).write(1, 20).write(2, 0);

                PacketDataSerializer dataSerializer = new PacketDataSerializer(Unpooled.buffer());
                try {
                    PacketPlayOutTitle titlePacket = new PacketPlayOutTitle() {
                        @Override
                        public void b(PacketDataSerializer var0) {
                            var0.a(EnumTitleAction.TITLE);
                            var0.a(stringBuilder.toString());
                        }
                    };
                    titlePacket.b(dataSerializer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                byte[] bytes = new byte[dataSerializer.readableBytes()];
                dataSerializer.readBytes(bytes);

                Bukkit.getOnlinePlayers().forEach(x -> {
                    try {
                        protocolManager.sendServerPacket(x, timePacket);
                        protocolManager.sendWirePacket(x, 0x50, bytes);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            }
        }.runTaskTimer(this, 0, 1);

        PlayerChatHook.setup();
    }

    private float hue = 0;
    private Color getRainbowColor() {
        this.hue = (this.hue + 4) % 362;
        return  Color.getHSBColor(this.hue / 360F, 1.0F, 1.0F);
    }

    @Override
    public void onDisable() {
        this.managers.values().forEach(Manager::disable);
        this.managers.clear();
    }

    /**
     * Gets a manager instance
     *
     * @param managerClass The class of the manager instance to get
     * @param <T> The manager type
     * @return The manager instance or null if one does not exist
     */
    @SuppressWarnings("unchecked")
    public <T extends Manager> T getManager(Class<T> managerClass) {
        if (this.managers.containsKey(managerClass))
            return (T) this.managers.get(managerClass);

        try {
            T manager = managerClass.getConstructor(this.getClass()).newInstance(this);
            this.managers.put(managerClass, manager);
            manager.reload();
            return manager;
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the file which contains this plugin
     * Exposes the JavaPlugin.getFile() method
     *
     * @return File containing this plugin
     */
    public File getJarFile() {
        return this.getFile();
    }
    
    /**
     * Reloads the plugin
     */
    public void reload() {
        this.managers.values().forEach(Manager::disable);
        this.managers.values().forEach(Manager::reload);

        this.getManager(CommandManager.class);
        this.getManager(ParticleStyleManager.class);
        this.getManager(ParticleGroupPresetManager.class);
        this.getManager(ConfigurationManager.class);
        this.getManager(DataManager.class);
        this.getManager(DataMigrationManager.class);
        this.getManager(ParticleManager.class);
        this.getManager(LocaleManager.class);
        this.getManager(ConfigurationManager.class);
        this.getManager(PermissionManager.class);
        this.getManager(PluginUpdateManager.class);
    }

    /**
     * Gets the instance of the plugin running on the server
     * 
     * @return The PlayerParticles plugin instance
     */
    public static PlayerParticles getInstance() {
        return INSTANCE;
    }

}
