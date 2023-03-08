package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.pack.ParticlePack;
import dev.esophose.playerparticles.pack.ParticlePackDescription;
import dev.esophose.playerparticles.pack.ParticlePackInitializationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ParticlePackManager extends Manager {

    public static final String PACK_DIRECTORY_NAME = "packs";
    public static final String INFO_FILE_NAME = "particle_pack.yml";
    private final Map<String, ParticlePack> loadedPacks;

    public ParticlePackManager(RosePlugin playerParticles) {
        super(playerParticles);
        this.loadedPacks = new HashMap<>();
    }

    @Override
    public void reload() {
        this.moveParticlePacksFromPluginsFolder();

        File packDirectory = new File(this.rosePlugin.getDataFolder(), PACK_DIRECTORY_NAME);
        if (!packDirectory.exists())
            packDirectory.mkdirs();

        File[] files = packDirectory.listFiles();
        if (files == null || files.length == 0)
            return;

        for (File file : files) {
            if (!file.isFile() || !file.getName().endsWith(".jar") || !this.isParticlePack(file))
                continue;

            ParticlePack particlePack = this.load(file);
            if (particlePack == null)
                continue;

            ParticlePackDescription description = particlePack.getDescription();
            if (description.getName() == null || description.getName().isEmpty()) {
                this.rosePlugin.getLogger().warning("Particle pack '" + file.getName() + "' has no name, skipping!");
                continue;
            }

            if (description.getVersion() == null || description.getVersion().isEmpty()) {
                this.rosePlugin.getLogger().warning("Particle pack '" + description.getName() + "' has no version, skipping!");
                continue;
            }

            ParticlePack possibleDuplicate = this.getParticlePack(description.getName());
            if (possibleDuplicate != null) {
                this.rosePlugin.getLogger().warning("Found duplicate particle pack '" + description.getName() + "', overwriting!");
                this.loadedPacks.values().remove(possibleDuplicate);
            }

            this.loadedPacks.put(particlePack.getName(), particlePack);
            this.rosePlugin.getLogger().info("Loaded particle pack '" + particlePack.getName() + " v" + particlePack.getDescription().getVersion() + "' with " + particlePack.getNumberOfStyles() + " style" + (particlePack.getNumberOfStyles() > 1 ? "s" : ""));
        }

        int numStyles = this.loadedPacks.values().stream().mapToInt(ParticlePack::getNumberOfStyles).sum();
        this.rosePlugin.getLogger().info("Loaded " + this.loadedPacks.size() + " particle pack" + (this.loadedPacks.size() > 1 ? "s" : "") + " with " + numStyles + " style" + (numStyles > 1 ? "s" : ""));
    }

    @Override
    public void disable() {
        this.loadedPacks.values().forEach(particlePack -> {
            try {
                particlePack.getClassLoader().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.loadedPacks.clear();
    }

    /**
     * Gets the particle pack with the given name, case-insensitive
     *
     * @param packName The name of the particle pack
     * @return The particle pack with the given name, or null if not found
     */
    public ParticlePack getParticlePack(String packName) {
        return this.loadedPacks.entrySet().stream()
                .filter(x -> x.getKey().equalsIgnoreCase(packName))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * @return A collection of all loaded particle packs
     */
    public Collection<ParticlePack> getLoadedParticlePacks() {
        return this.loadedPacks.values();
    }

    /**
     * Attempts to load a particle pack from the given file
     *
     * @param packJar The file to load the particle pack from
     * @return The loaded particle pack, or null if the file is not a valid particle pack
     */
    public ParticlePack load(File packJar) {
        try {
            URL jar = packJar.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jar}, this.getClass().getClassLoader());
            List<String> matches = new ArrayList<>();
            List<Class<? extends ParticlePack>> classes = new ArrayList<>();

            try (JarInputStream jarInputStream = new JarInputStream(jar.openStream())) {
                JarEntry entry;
                while ((entry = jarInputStream.getNextJarEntry()) != null) {
                    String name = entry.getName();
                    if (name.endsWith(".class"))
                        matches.add(name.substring(0, name.lastIndexOf('.')).replace('/', '.'));
                }
            }

            for (String match : matches) {
                try {
                    Class<?> clazz = classLoader.loadClass(match);
                    if (ParticlePack.class.isAssignableFrom(clazz))
                        classes.add(clazz.asSubclass(ParticlePack.class));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (classes.isEmpty()) {
                this.rosePlugin.getLogger().warning("Failed to load particle pack " + packJar.getName() + ": No particle pack found");
                classLoader.close();
                return null;
            }

            if (classes.size() > 1) {
                this.rosePlugin.getLogger().warning("Failed to load particle pack " + packJar.getName() + ": Multiple particle packs found");
                return null;
            }

            try (InputStream inputStream = new URL("jar:file:" + packJar.getAbsolutePath() + "!/" + INFO_FILE_NAME).openStream();
                 Reader fileReader = new InputStreamReader(inputStream)) {
                FileConfiguration particlePackConfig = YamlConfiguration.loadConfiguration(fileReader);
                ParticlePack particlePack = classes.get(0).getDeclaredConstructor().newInstance();
                Method initMethod = ParticlePack.class.getDeclaredMethod("init", PlayerParticles.class, ParticlePackDescription.class, URLClassLoader.class);
                initMethod.setAccessible(true);
                initMethod.invoke(particlePack, (PlayerParticles) this.rosePlugin, new ParticlePackDescription(particlePackConfig), classLoader);
                return particlePack;
            }
        } catch (Exception e) {
            throw new ParticlePackInitializationException(packJar.getName(), e);
        }
    }

    /**
     * Attempts to unload a particle pack with the given name
     *
     * @param packName The name of the particle pack to unload
     * @return True if the particle pack was unloaded, false if it was not found or something went wrong
     */
    public boolean unload(String packName) {
        ParticlePack particlePack = this.getParticlePack(packName);
        if (particlePack == null)
            return false;

        try {
            ParticleStyleManager particleStyleManager = this.rosePlugin.getManager(ParticleStyleManager.class);
            particlePack.getStyles().forEach(particleStyleManager::removeAllStyleReferences);
            particlePack.getEventStyles().forEach(particleStyleManager::removeAllStyleReferences);

            this.loadedPacks.remove(particlePack.getName());
            particlePack.getClassLoader().close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Moves particle packs in the plugins directory to the packs directory
     */
    private void moveParticlePacksFromPluginsFolder() {
        File pluginsDirectory = this.rosePlugin.getDataFolder().getParentFile();
        File[] files = pluginsDirectory.listFiles();
        if (files == null || files.length == 0)
            return;

        for (File file : files) {
            if (!file.isFile() || !file.getName().endsWith(".jar"))
                continue;

            if (this.isParticlePack(file)) {
                this.rosePlugin.getLogger().warning("Found particle pack in plugins directory, attempting to move to packs directory: " + file.getName());

                // Move the jar to the packs folder
                File newFile = new File(this.rosePlugin.getDataFolder(), PACK_DIRECTORY_NAME + File.separator + file.getName());
                if (newFile.exists()) {
                    this.rosePlugin.getLogger().warning("Found duplicate particle pack when moving to packs directory, deleting old version: " + file.getName());
                    newFile.delete();
                }

                file.renameTo(newFile);
            }
        }
    }

    /**
     * Checks if the given file is a particle pack
     *
     * @param file The file to check
     * @return True if the file is a particle pack, false otherwise
     */
    private boolean isParticlePack(File file) {
        try {
            // Try to find a file named "particle_pack.yml" in the jar and immediately discard it
            new URL("jar:file:" + file.getAbsolutePath() + "!/" + INFO_FILE_NAME).openStream().close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
