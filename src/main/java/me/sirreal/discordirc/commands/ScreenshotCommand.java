package me.sirreal.discordirc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.sirreal.discordirc.config.Config;
import me.sirreal.discordirc.util.MinecraftChat;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

public class ScreenshotCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        dispatcher.register(ClientCommandManager.literal("screenshot")
                .executes(ScreenshotCommand::toggleScreenshots)
                .then(ClientCommandManager.literal("delete")
                        .executes(ScreenshotCommand::toggleDeleteAfterSending)));

        dispatcher.register(ClientCommandManager.literal("sc")
                .executes(ScreenshotCommand::toggleScreenshots)
                .then(ClientCommandManager.literal("delete")
                        .executes(ScreenshotCommand::toggleDeleteAfterSending)));
    }

    private static int toggleScreenshots(CommandContext<FabricClientCommandSource> ctx) {
        Config.screenshotsEnabled = !Config.screenshotsEnabled;
        MinecraftChat.sendChatMessage(Text.of("§e[IRC] §fSending screenshots is now " + (Config.screenshotsEnabled ? "§aenabled" : "§cdisabled") + "."));
        Config.saveConfig();
        return 1;
    }

    private static int toggleDeleteAfterSending(CommandContext<FabricClientCommandSource> ctx) {
        Config.deleteAfterSending = !Config.deleteAfterSending;
        MinecraftChat.sendChatMessage(Text.of("§e[IRC] §fDeleting screenshots after sending is now " + (Config.deleteAfterSending ? "§aenabled" : "§cdisabled") + "."));
        Config.saveConfig();
        return 1;
    }
}
