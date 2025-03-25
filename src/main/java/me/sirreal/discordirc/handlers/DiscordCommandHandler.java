package me.sirreal.discordirc.handlers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class DiscordCommandHandler {

    public static void initialize() {
        String command = "/logout";
        String playerId = "uniquePlayerId";

        // Call the function to process the command
        processDiscordCommand(command, playerId);
    }

    public static void processDiscordCommand(String command, String playerId) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (command.equals("/logout")) {
            if (client.player != null && client.player.getName().getString().equals(playerId)) {
                client.getNetworkHandler().getConnection().disconnect(Text.of("You have logged out via Discord."));
            }
        }

    }
}