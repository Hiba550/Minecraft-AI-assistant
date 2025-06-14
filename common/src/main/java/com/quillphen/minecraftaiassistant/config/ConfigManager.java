package com.quillphen.minecraftaiassistant.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quillphen.minecraftaiassistant.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private static final String CONFIG_FILE = "minecraft_ai_assistant.json";
    private static AIConfig config;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static AIConfig getConfig() {
        if (config == null) {
            config = loadConfig();
        }
        return config;
    }

    public static void saveConfig() {
        if (config == null) return;
        
        try {
            Path configDir = getConfigDir();
            Files.createDirectories(configDir);
            
            Path configFile = configDir.resolve(CONFIG_FILE);
            String json = GSON.toJson(config);
            Files.write(configFile, json.getBytes());
            
            Constants.LOG.info("AI Assistant config saved to: {}", configFile);
        } catch (IOException e) {
            Constants.LOG.error("Failed to save AI Assistant config", e);
        }
    }

    private static AIConfig loadConfig() {
        try {
            Path configFile = getConfigDir().resolve(CONFIG_FILE);
            if (Files.exists(configFile)) {
                String json = Files.readString(configFile);
                AIConfig loadedConfig = GSON.fromJson(json, AIConfig.class);
                Constants.LOG.info("AI Assistant config loaded from: {}", configFile);
                return loadedConfig != null ? loadedConfig : new AIConfig();
            }
        } catch (IOException e) {
            Constants.LOG.error("Failed to load AI Assistant config", e);
        }
        
        return new AIConfig();
    }

    private static Path getConfigDir() {
        // Try to use Minecraft's config directory
        String configPath = System.getProperty("user.home");
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return Paths.get(configPath, "AppData", "Roaming", ".minecraft", "config");
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            return Paths.get(configPath, "Library", "Application Support", "minecraft", "config");
        } else {
            return Paths.get(configPath, ".minecraft", "config");
        }
    }
}
