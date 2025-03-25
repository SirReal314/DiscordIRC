package me.sirreal.discordirc.config;

import com.google.gson.*;
import me.sirreal.discordirc.handlers.WebSocketHandler;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class Config {
    private static final File configFile = new File("config/discordirc.json");

    public static boolean modEnabled = true;
    public static boolean mcToDcEnabled = true;
    public static boolean dcToMcEnabled = true;
    public static String apiUrl = "";

    public static boolean screenshotsEnabled = false;
    public static boolean deleteAfterSending = false;

    public static boolean commandExecutionEnabled = false;
    public static boolean logoutCommandEnabled = false;
    public static boolean killCommandEnabled = false;
    public static boolean screenshotCommandEnabled = false;
    public static boolean sayCommandEnabled = false;
    public static boolean sudoCommandEnabled = false;

    public static boolean clientCommandsEnabled = false;
    public static boolean coordsCommandEnabled = false;
    public static boolean afkCommandEnabled = false;

    public static boolean deathMessagesEnabled = false;
    public static String deathMessageTitle = "You died, {name}";
    public static boolean deathMessageCoordinates = false;
    public static boolean deathMessageWorldName = false;
    public static boolean deathMessageUseDm = false;
    public static boolean deathMessageMention = false;
    public static String deathMessageUserId = "";
    public static String deathMessageEmbedColor = "#FF0000";

    public static boolean playerDetectionEnabled = false;
    public static String playerDetectionMessageTitle = "Saw player: {name}";
    public static boolean playerDetectionMessageCoordinates = false;
    public static boolean playerDetectionMessageWorldName = false;
    public static boolean playerDetectionMessageUseDm = false;
    public static boolean playerDetectionMessageMention = false;
    public static String playerDetectionMessageUserId = "";
    public static String playerDetectionMessageEmbedColor = "#FF0000";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Config.class, new ConfigAdapter())
            .registerTypeAdapter(String.class, new ColorAdapter())  // Register ColorAdapter for hex string
            .setPrettyPrinting()
            .create();

    public static void loadConfig() {
        if (!configFile.exists()) {
            saveConfig(); // Create a default config if not present
        }

        try (Reader reader = new FileReader(configFile)) {
            Config config = gson.fromJson(reader, Config.class);
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }

    public static void saveConfig() {
        try (Writer writer = new FileWriter(configFile)) {
            gson.toJson(new Config(), writer);
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }

    // Custom TypeAdapter for Config class to serialize and deserialize static fields dynamically
    private static class ConfigAdapter implements JsonSerializer<Config>, JsonDeserializer<Config> {
        @Override
        public JsonElement serialize(Config config, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();

            // Use reflection to serialize static fields
            for (Field field : Config.class.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(null);
                        if (value instanceof String) {  // For hex color strings
                            jsonObject.add(field.getName(), new JsonPrimitive((String) value));
                        } else {
                            jsonObject.add(field.getName(), context.serialize(value));
                        }
                    } catch (IllegalAccessException e) {
                        System.err.println("Error serializing field: " + field.getName());
                    }
                }
            }
            return jsonObject;
        }

        @Override
        public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            // Use reflection to deserialize static fields
            for (Field field : Config.class.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                    field.setAccessible(true);
                    try {
                        JsonElement element = jsonObject.get(field.getName());
                        if (element != null) {
                            if (field.getType() == String.class) {  // Hex color strings
                                field.set(null, element.getAsString());
                            } else {
                                field.set(null, context.deserialize(element, field.getType()));
                            }
                        }
                    } catch (IllegalAccessException e) {
                        System.err.println("Error deserializing field: " + field.getName());
                    }
                }
            }
            return new Config();
        }
    }

    static class ColorAdapter implements JsonSerializer<String>, JsonDeserializer<String> {
        @Override
        public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }

        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json.getAsString();
        }
    }
}
