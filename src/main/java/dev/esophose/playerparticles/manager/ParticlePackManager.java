package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.pack.ParticlePack;
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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ParticlePackManager extends Manager {

    public static final String PACK_DIRECTORY_NAME = "packs";
    public static final String INFO_FILE_NAME = "particle_pack.yml";
    private final Map<String, ParticlePack> loadedPacks;

    public ParticlePackManager(PlayerParticles playerParticles) {
        super(playerParticles);
        this.loadedPacks = new HashMap<>();
    }

    @Override
    public void reload() {
        this.moveParticlePacksFromPluginsFolder();

        File packDirectory = new File(this.playerParticles.getDataFolder(), PACK_DIRECTORY_NAME);
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

            this.loadedPacks.put(particlePack.getName(), particlePack);
            this.playerParticles.getLogger().info("Loaded particle pack '" + particlePack.getName() + "' with " + particlePack.getNumberOfStyles() + " styles");
        }

        this.playerParticles.getLogger().info("Loaded " + this.loadedPacks.size() + " particle pack" + (this.loadedPacks.size() > 1 ? "s" : "") + " with " + this.loadedPacks.values().stream().mapToInt(ParticlePack::getNumberOfStyles).sum() + " styles");
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

    public Collection<ParticlePack> getLoadedParticlePacks() {
        return this.loadedPacks.values();
    }

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
            } catch (IOException e) {
                e.printStackTrace();
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
                this.playerParticles.getLogger().warning("Failed to load particle pack " + packJar.getName() + ": No particle pack found");
                classLoader.close();
                return null;
            }

            if (classes.size() > 1) {
                this.playerParticles.getLogger().warning("Failed to load particle pack " + packJar.getName() + ": Multiple particle packs found");
                return null;
            }

            try (InputStream inputStream = new URL("jar:file:" + packJar.getAbsolutePath() + "!/" + INFO_FILE_NAME).openStream();
                 Reader fileReader = new InputStreamReader(inputStream)) {
                FileConfiguration particlePackConfig = YamlConfiguration.loadConfiguration(fileReader);
                ParticlePack particlePack = classes.get(0).getDeclaredConstructor().newInstance();
                Method initMethod = ParticlePack.class.getDeclaredMethod("init", PlayerParticles.class, FileConfiguration.class, URLClassLoader.class);
                initMethod.setAccessible(true);
                initMethod.invoke(particlePack, this.playerParticles, particlePackConfig, classLoader);
                return particlePack;
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean unload(String packName) {
        ParticlePack particlePack = this.loadedPacks.entrySet().stream().filter(x -> x.getKey().equalsIgnoreCase(packName)).map(Map.Entry::getValue).findFirst().orElse(null);
        if (particlePack == null)
            return false;

        try {
            ParticleStyleManager particleStyleManager = this.playerParticles.getManager(ParticleStyleManager.class);
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

    private void moveParticlePacksFromPluginsFolder() {
        File pluginsDirectory = this.playerParticles.getDataFolder().getParentFile();
        File[] files = pluginsDirectory.listFiles();
        if (files == null || files.length == 0)
            return;

        for (File file : files) {
            if (!file.isFile() || !file.getName().endsWith(".jar"))
                continue;

            if (this.isParticlePack(file)) {
                this.playerParticles.getLogger().warning("Found particle pack in plugins directory, attempting to move to packs directory: " + file.getName());

                // Move the jar to the packs folder
                File newFile = new File(this.playerParticles.getDataFolder(), PACK_DIRECTORY_NAME + "/" + file.getName());
                if (newFile.exists()) {
                    this.playerParticles.getLogger().warning("Found duplicate particle pack when moving to packs directory, deleting old version: " + file.getName());
                    newFile.delete();
                }

                file.renameTo(newFile);
            }
        }
    }

    private boolean isParticlePack(File file) {
        try {
            // Try to find a file named "particle_pack.yml" in the jar
            InputStream inputStream = new URL("jar:file:" + file.getAbsolutePath() + "!/" + INFO_FILE_NAME).openStream();

            // Don't actually need the file, just wanted to check if it exists
            inputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
