package me.TreeOfSelf.PandaBlockName;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PandaBlockNameConfig {
    private static final File CONFIG_FILE = new File("./config/PandaBlockName.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Map<String, Boolean> FEATURES = new HashMap<>() {{
        // Core Features
        put("Block", true);
        put("Crafting", true);
        put("Enderman", true);
        
        // Vegetation Features (Master Toggle)
        put("Vegetation", true);
        
        // Individual Vegetation Features
        put("BambooGrowth", true);
        put("CactusGrowth", true);
        put("SugarCaneGrowth", true);
        put("SaplingGrowth", true);
        put("StemGrowth", true);
        put("StemPlantGrowth", true);
        put("SeaPickleGrowth", true);
        put("VineGrowth", true);
        put("ChorusFlowerGrowth", true);
        put("PumpkinShearing", true);
        
        // Mushroom/Fungus Features
        put("MushroomGrowth", true);
        put("HugeBrownMushroomGeneration", true);
        put("HugeRedMushroomGeneration", true);
        put("HugeMushroomFeatures", true);
        put("HugeFungusGeneration", true);
    }};

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);

                JsonObject updatedJson = new JsonObject();

                for (String feature : FEATURES.keySet()) {
                    boolean value;
                    if (json.has(feature)) {
                        value = json.get(feature).getAsBoolean();
                        FEATURES.put(feature, value);
                    } else {
                        value = FEATURES.get(feature);
                    }
                    updatedJson.addProperty(feature, value);
                }

                saveConfigToFile(updatedJson);
            } catch (IOException ignored) {
            }
        } else {
            createDefaultConfig();
        }
    }

    private static void createDefaultConfig() {
        JsonObject json = new JsonObject();
        FEATURES.forEach((feature, defaultValue) -> json.addProperty(feature, defaultValue));
        saveConfigToFile(json);
    }

    private static void saveConfigToFile(JsonObject json) {
        try {
            if (!CONFIG_FILE.getParentFile().exists()) {
                CONFIG_FILE.getParentFile().mkdirs();
            }
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(json, writer);
            }
        } catch (IOException ignored) {
        }
    }

    public static boolean isFeatureEnabled(String feature) {
        return FEATURES.getOrDefault(feature, false);
    }
    
    public static boolean isVegetationFeatureEnabled(String feature) {
        return isFeatureEnabled("Vegetation") && isFeatureEnabled(feature);
    }
}