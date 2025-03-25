package me.sirreal.discordirc.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class MinecraftChat {
    public static MinecraftClient getMC() {
        return MinecraftClient.getInstance();
    }

    public static String getUsername() {
        return getMC().getSession().getUsername();
    }

    public static void sendChatMessage(Text message) {
        if (getMC().inGameHud != null)
            getMC().inGameHud.getChatHud().addMessage(message);
    }

    public static void print(String message) {
        sendChatMessage(Text.of(message));
    }
}
