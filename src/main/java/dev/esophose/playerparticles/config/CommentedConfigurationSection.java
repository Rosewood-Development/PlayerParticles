package dev.esophose.playerparticles.config;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CommentedConfigurationSection implements ConfigurationSection {

    protected ConfigurationSection config;

    public CommentedConfigurationSection(ConfigurationSection configuration) {
        this.config = configuration;
    }

    /**
     * Gets a defaulted boolean value. These accept values of either "default", true, or false
     *
     * @param path The value key
     * @return null for "default", otherwise true or false
     */
    public Boolean getDefaultedBoolean(String path) {
        if (this.isBoolean(path)) {
            return this.getBoolean(path);
        } else if (this.isString(path)) {
            String stringValue = this.getString(path);
            if (stringValue != null && stringValue.equalsIgnoreCase("default"))
                return null;
        }

        return null;
    }

    /**
     * Gets a defaulted boolean value. These accept values of either "default", true, or false
     *
     * @param path The value key
     * @param def The value to return if the key is not found
     * @return null for "default", otherwise true or false
     */
    public Boolean getDefaultedBoolean(String path, Boolean def) {
        Object value = this.get(path);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            if (stringValue.equalsIgnoreCase("default"))
                return null;
        }

        if (value == null)
            return def;

        return null;
    }

    @Override
    public Set<String> getKeys(boolean b) {
        Set<String> keys = new LinkedHashSet<>(this.config.getKeys(b));
        keys.removeIf(x -> x.contains("_COMMENT_"));
        return keys;
    }

    @Override
    public Map<String, Object> getValues(boolean b) {
        return this.config.getValues(b);
    }

    @Override
    public boolean contains(String s) {
        return this.config.contains(s);
    }

    @Override
    public boolean contains(String s, boolean b) {
        return this.config.contains(s, b);
    }

    @Override
    public boolean isSet(String s) {
        return this.config.isSet(s);
    }

    @Override
    public String getCurrentPath() {
        return this.config.getCurrentPath();
    }

    @Override
    public String getName() {
        return this.config.getName();
    }

    @Override
    public Configuration getRoot() {
        return this.config.getRoot();
    }

    @Override
    public ConfigurationSection getParent() {
        return this.config.getParent();
    }

    @Override
    public Object get(String s) {
        return this.config.get(s);
    }

    @Override
    public Object get(String s, Object o) {
        return this.config.get(s, o);
    }

    @Override
    public void set(String s, Object o) {
        this.config.set(s, o);
    }

    @Override
    public CommentedConfigurationSection createSection(String s) {
        return new CommentedConfigurationSection(this.config.createSection(s));
    }

    @Override
    public CommentedConfigurationSection createSection(String s, Map<?, ?> map) {
        return new CommentedConfigurationSection(this.config.createSection(s, map));
    }

    @Override
    public String getString(String s) {
        return this.config.getString(s);
    }

    @Override
    public String getString(String s, String s1) {
        return this.config.getString(s, s1);
    }

    @Override
    public boolean isString(String s) {
        return this.config.isString(s);
    }

    @Override
    public int getInt(String s) {
        return this.config.getInt(s);
    }

    @Override
    public int getInt(String s, int i) {
        return this.config.getInt(s, i);
    }

    @Override
    public boolean isInt(String s) {
        return this.config.isInt(s);
    }

    @Override
    public boolean getBoolean(String s) {
        return this.config.getBoolean(s);
    }

    @Override
    public boolean getBoolean(String s, boolean b) {
        return this.config.getBoolean(s, b);
    }

    @Override
    public boolean isBoolean(String s) {
        return this.config.isBoolean(s);
    }

    @Override
    public double getDouble(String s) {
        return this.config.getDouble(s);
    }

    @Override
    public double getDouble(String s, double v) {
        return this.config.getDouble(s, v);
    }

    @Override
    public boolean isDouble(String s) {
        return this.config.isDouble(s);
    }

    @Override
    public long getLong(String s) {
        return this.config.getLong(s);
    }

    @Override
    public long getLong(String s, long l) {
        return this.config.getLong(s, l);
    }

    @Override
    public boolean isLong(String s) {
        return this.config.isLong(s);
    }

    @Override
    public List<?> getList(String s) {
        return this.config.getList(s);
    }

    @Override
    public List<?> getList(String s, List<?> list) {
        return this.config.getList(s, list);
    }

    @Override
    public boolean isList(String s) {
        return this.config.isList(s);
    }

    @Override
    public List<String> getStringList(String s) {
        return this.config.getStringList(s);
    }

    @Override
    public List<Integer> getIntegerList(String s) {
        return this.config.getIntegerList(s);
    }

    @Override
    public List<Boolean> getBooleanList(String s) {
        return this.config.getBooleanList(s);
    }

    @Override
    public List<Double> getDoubleList(String s) {
        return this.config.getDoubleList(s);
    }

    @Override
    public List<Float> getFloatList(String s) {
        return this.config.getFloatList(s);
    }

    @Override
    public List<Long> getLongList(String s) {
        return this.config.getLongList(s);
    }

    @Override
    public List<Byte> getByteList(String s) {
        return this.config.getByteList(s);
    }

    @Override
    public List<Character> getCharacterList(String s) {
        return this.config.getCharacterList(s);
    }

    @Override
    public List<Short> getShortList(String s) {
        return this.config.getShortList(s);
    }

    @Override
    public List<Map<?, ?>> getMapList(String s) {
        return this.config.getMapList(s);
    }

    @Override
    public <T> T getObject(String s, Class<T> aClass) {
        return this.config.getObject(s, aClass);
    }

    @Override
    public <T> T getObject(String s, Class<T> aClass, T t) {
        return this.config.getObject(s, aClass, t);
    }

    @Override
    public <T extends ConfigurationSerializable> T getSerializable(String s, Class<T> aClass) {
        return this.config.getSerializable(s, aClass);
    }

    @Override
    public <T extends ConfigurationSerializable> T getSerializable(String s, Class<T> aClass, T t) {
        return this.config.getSerializable(s, aClass, t);
    }

    @Override
    public Vector getVector(String s) {
        return this.config.getVector(s);
    }

    @Override
    public Vector getVector(String s, Vector vector) {
        return this.config.getVector(s, vector);
    }

    @Override
    public boolean isVector(String s) {
        return this.config.isVector(s);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String s) {
        return this.config.getOfflinePlayer(s);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String s, OfflinePlayer offlinePlayer) {
        return this.config.getOfflinePlayer(s, offlinePlayer);
    }

    @Override
    public boolean isOfflinePlayer(String s) {
        return this.config.isOfflinePlayer(s);
    }

    @Override
    public ItemStack getItemStack(String s) {
        return this.config.getItemStack(s);
    }

    @Override
    public ItemStack getItemStack(String s, ItemStack itemStack) {
        return this.config.getItemStack(s, itemStack);
    }

    @Override
    public boolean isItemStack(String s) {
        return this.config.isItemStack(s);
    }

    @Override
    public Color getColor(String s) {
        return this.config.getColor(s);
    }

    @Override
    public Color getColor(String s, Color color) {
        return this.config.getColor(s, color);
    }

    @Override
    public boolean isColor(String s) {
        return this.config.isColor(s);
    }

    @Override
    public Location getLocation(String path) {
        return this.getSerializable(path, Location.class);
    }

    @Override
    public Location getLocation(String path, Location def) {
        return this.getSerializable(path, Location.class, def);
    }

    @Override
    public boolean isLocation(String path) {
        return this.getSerializable(path, Location.class) != null;
    }

    @Override
    public CommentedConfigurationSection getConfigurationSection(String s) {
        ConfigurationSection section = this.config.getConfigurationSection(s);
        if (section == null)
            return this.createSection(s);

        return new CommentedConfigurationSection(section);
    }

    @Override
    public boolean isConfigurationSection(String s) {
        return this.config.isConfigurationSection(s);
    }

    @Override
    public CommentedConfigurationSection getDefaultSection() {
        return new CommentedConfigurationSection(this.config.getDefaultSection());
    }

    @Override
    public void addDefault(String s, Object o) {
        this.config.addDefault(s, o);
    }

}
