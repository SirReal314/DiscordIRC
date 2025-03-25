package me.sirreal.discordirc.handlers;

import com.google.gson.Gson;
import me.sirreal.discordirc.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

import java.text.DecimalFormat;

public class EventHandler {
    private static final Gson gson = new Gson();

    public static void sendDeathMessage(String deathMessageText) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null || !Config.deathMessagesEnabled) return;

        String playerName = client.player.getGameProfile().getName();
        DecimalFormat df = new DecimalFormat("###.##");

        String title = Config.deathMessageTitle.replace("{name}", playerName);

        String mention = "";
        if (Config.deathMessageMention && !Config.deathMessageUserId.isEmpty()) {
            mention = "<@" + Config.deathMessageUserId + "> ";
        }
        Field[] fields = buildFields(client, df);

        EmbedObject embed = new EmbedObject(
                title,
                deathMessageText,
                parseColor(Config.deathMessageEmbedColor),
                fields
        );
        EmbedMessage embedMessage = new EmbedMessage(
                playerName,
                mention,
                embed
        );

        String payload = gson.toJson(embedMessage);

        if (Config.deathMessageUseDm) {
            sendDirectMessage(payload);
        } else {
            WebSocketHandler.sendMessage(payload);
        }
    }

    private static Field[] buildFields(MinecraftClient client, DecimalFormat df) {
        Field[] fields;
        boolean hasCoordinates = Config.deathMessageCoordinates;
        boolean hasWorldName = Config.deathMessageWorldName;

        assert client.world != null;
        assert client.player != null;
        if (hasCoordinates && hasWorldName) {
            fields = new Field[]{
                    new Field("Coordinates",
                            "X: " + df.format(client.player.getX()) +
                                    " Y: " + df.format(client.player.getY()) +
                                    " Z: " + df.format(client.player.getZ()), false),
                    new Field("World", getWorldName(client.world), false)
            };
        } else if (hasCoordinates) {
            fields = new Field[]{
                    new Field("Coordinates",
                            "X: " + df.format(client.player.getX()) +
                                    " Y: " + df.format(client.player.getY()) +
                                    " Z: " + df.format(client.player.getZ()), false)
            };
        } else if (hasWorldName) {
            fields = new Field[]{
                    new Field("World", getWorldName(client.world), false)
            };
        } else {
            fields = new Field[]{};
        }
        return fields;
    }

    private static String getWorldName(ClientWorld world) {
        String worldName = world.getRegistryKey().getValue().getPath();
        return switch (worldName) {
            case "the_nether" -> "Nether";
            case "the_end" -> "The End";
            default -> "Overworld";
        };
    }

    private static int parseColor(String hexColor) {
        try {
            return Integer.parseInt(hexColor.replace("#", ""), 16);
        } catch (NumberFormatException e) {
            System.err.println("Invalid embed color format: " + hexColor);
            return 0xFF0000;
        }
    }

    private static void sendDirectMessage(String payload) {
        System.out.println("Sending DM: " + payload);
        WebSocketHandler.sendMessage(payload);
    }

    private static class EmbedMessage {
        String username;
        String message;
        EmbedObject embed;

        public EmbedMessage(String username, String message, EmbedObject embed) {
            this.username = username;
            this.message = message;
            this.embed = embed;
        }
    }

    private static class EmbedObject {
        String title;
        String description;
        int color;
        Field[] fields;

        public EmbedObject(String title, String description, int color, Field[] fields) {
            this.title = title;
            this.description = description;
            this.color = color;
            this.fields = fields;
        }
    }

    private static class Field {
        String name;
        String value;
        boolean inline;

        public Field(String name, String value, boolean inline) {
            this.name = name;
            this.value = value;
            this.inline = inline;
        }
    }
}
