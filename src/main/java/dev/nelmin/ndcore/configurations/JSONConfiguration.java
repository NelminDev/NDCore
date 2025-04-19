package dev.nelmin.ndcore.configurations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Configuration class for handling JSON format configurations
 */
public class JSONConfiguration extends FileConfiguration {

    private Gson gson;

    /**
     * Creates a new JSONConfiguration with default Gson instance
     */
    public JSONConfiguration() {
        this.gson = new Gson();
    }

    /**
     * Loads configuration from a file
     *
     * @param file File to load from
     * @return Loaded configuration
     * @throws IllegalArgumentException if file is null
     */
    public static @NotNull JSONConfiguration loadConfiguration(@NotNull File file) {
        Objects.requireNonNull(file, "File cannot be null");
        JSONConfiguration config = new JSONConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().log(Level.SEVERE, "File not found: " + file, e);
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, e);
        }

        return config;
    }

    /**
     * Loads configuration from a reader
     *
     * @param reader Reader to load from
     * @return Loaded configuration
     * @throws IllegalArgumentException if reader is null
     */
    public static @NotNull JSONConfiguration loadConfiguration(@NotNull Reader reader) {
        Objects.requireNonNull(reader, "Reader cannot be null");
        JSONConfiguration config = new JSONConfiguration();

        try {
            config.load(reader);
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", e);
        }

        return config;
    }

    /**
     * Saves configuration to a JSON string
     *
     * @return JSON string representation of the configuration
     */
    @NotNull
    @Override
    public String saveToString() {
        Map<String, Object> values = convertSectionsToMaps(this.getValues(false));
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().serializeNulls();
        if (this.options().prettyPrinting()) {
            gsonBuilder.setPrettyPrinting();
        }
        this.gson = gsonBuilder.create();
        return this.gson.toJson(values);
    }

    /**
     * Loads configuration from a JSON string
     *
     * @param contents The string contents to load from
     * @throws InvalidConfigurationException When the string is not valid JSON
     */
    @Override
    public void loadFromString(@NotNull String contents) throws InvalidConfigurationException {
        Objects.requireNonNull(contents, "Contents cannot be null");
        Map<String, Object> map;
        try {
            map = this.gson.fromJson(contents, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            throw new InvalidConfigurationException(e);
        }

        this.map.clear();
        if (map != null) {
            convertMapsToSections(map, this);
        }
    }

    /**
     * Converts configuration sections to maps
     *
     * @param input Map to convert
     * @return Converted map with sections as nested maps
     */
    private Map<String, Object> convertSectionsToMaps(Map<String, Object> input) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof ConfigurationSection section) {
                result.put(entry.getKey(), convertSectionsToMaps(section.getValues(false)));
            } else {
                result.put(entry.getKey(), value);
            }
        }
        return result;
    }

    /**
     * Converts maps to configuration sections
     *
     * @param input   Map to convert
     * @param section Parent configuration section
     */
    @SuppressWarnings("unchecked")
    private void convertMapsToSections(Map<String, Object> input, ConfigurationSection section) {
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                ConfigurationSection childSection = section.createSection(key);
                convertMapsToSections((Map<String, Object>) value, childSection);
            } else {
                section.set(key, value);
            }
        }
    }

    /**
     * Gets the options for this configuration
     *
     * @return Configuration options
     */
    @Override
    @NotNull
    public JSONConfiguration.Options options() {
        if (this.options == null) {
            this.options = new Options(this);
        }
        return (Options) this.options;
    }

    /**
     * Options class for JSON configuration
     */
    public static class Options extends FileConfigurationOptions {
        private boolean prettyPrinting = true;

        /**
         * Creates new JSON configuration options
         *
         * @param configuration The configuration
         */
        protected Options(JSONConfiguration configuration) {
            super(configuration);
        }

        /**
         * Sets whether to use pretty printing
         *
         * @param value True to enable pretty printing
         * @return This object
         */
        public Options prettyPrinting(boolean value) {
            this.prettyPrinting = value;
            return this;
        }

        /**
         * Gets whether pretty printing is enabled
         *
         * @return True if pretty printing is enabled
         */
        public boolean prettyPrinting() {
            return this.prettyPrinting;
        }

        /**
         * Gets the configuration instance
         *
         * @return The configuration
         */
        @Override
        @NotNull
        public JSONConfiguration configuration() {
            return (JSONConfiguration) super.configuration();
        }

        /**
         * Sets whether to copy defaults
         *
         * @param value True to copy defaults
         * @return This object
         */
        @Override
        @NotNull
        public JSONConfiguration.Options copyDefaults(boolean value) {
            super.copyDefaults(value);
            return this;
        }

        /**
         * Sets the path separator character
         *
         * @param value The separator character
         * @return This object
         */
        @Override
        @NotNull
        public JSONConfiguration.Options pathSeparator(char value) {
            super.pathSeparator(value);
            return this;
        }
    }
}