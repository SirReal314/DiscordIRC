package me.sirreal.discordirc.handlers;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatHandler {
    private static final Gson gson = new Gson();

    public static void handleIncomingMessage(String message) {
        try {
            ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player != null) {
                String formattedMessage;

                if ("minecraft".equals(chatMessage.source)) {
                    formattedMessage = String.format("§b[Discord] §f<%s> %s", chatMessage.username, chatMessage.message);
                }
                else if ("discord".equals(chatMessage.source)) {
                    formattedMessage = String.format("§b[Discord] %s: §f%s", chatMessage.username, chatMessage.message);
                } else {
                    formattedMessage = String.format("<%s> %s", chatMessage.username, chatMessage.message);
                }

                client.execute(() -> client.player.sendMessage(Text.of(formattedMessage), false));
            }
        } catch (Exception e) {
            System.err.println("Error handling incoming message: " + e.getMessage());
        }
    }

    public static boolean sendMessageToDiscord(String message) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return false;

            String username = client.player.getGameProfile().getName();
            String payload = gson.toJson(new ChatMessage(username, message, "minecraft"));
            return WebSocketHandler.sendMessage(payload);
        } catch (Exception e) {
            System.err.println("Error sending message to Discord: " + e.getMessage());
        }
        return false;
    }

    private static class ChatMessage {
        String username;
        String message;
        String source;

        public ChatMessage(String username, String message, String source) {
            this.username = username;
            this.message = message;
            this.source = source;
        }
    }
}
