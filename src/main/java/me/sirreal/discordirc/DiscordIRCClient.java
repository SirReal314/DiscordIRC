package me.sirreal.discordirc;

import me.sirreal.discordirc.commands.ScreenshotCommand;
import me.sirreal.discordirc.handlers.ChatHandler;
import me.sirreal.discordirc.commands.GlobalChatCommand;
import me.sirreal.discordirc.config.Config;
import me.sirreal.discordirc.handlers.EventHandler;
import me.sirreal.discordirc.handlers.WebSocketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class DiscordIRCClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        Config.loadConfig();
        WebSocketHandler.initialize();

        ClientCommandRegistrationCallback.EVENT.register(GlobalChatCommand::register);
        ClientCommandRegistrationCallback.EVENT.register(ScreenshotCommand::register);

        System.out.println("DiscordIRC Mod loaded successfully!");
    }
}