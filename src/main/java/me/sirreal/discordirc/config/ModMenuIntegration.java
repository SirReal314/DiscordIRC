package me.sirreal.discordirc.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import me.sirreal.discordirc.DiscordIRCClient;
import me.sirreal.discordirc.handlers.WebSocketHandler;
import net.minecraft.text.Text;

import java.awt.*;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Text.literal("DiscordIRC"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Discord IRC"))
                        .tooltip(Text.literal("Discord IRC"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("General"))
                                .description(OptionDescription.of(Text.literal("General options")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enabled"))
                                        .description(OptionDescription.of(Text.literal("Turns the mod on/off.")))
                                        .binding(Config.modEnabled, () -> Config.modEnabled, newVal -> {
                                            Config.modEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("MC → DC"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables sending Minecraft messages to Discord.")))
                                        .binding(Config.mcToDcEnabled, () -> Config.mcToDcEnabled, newVal -> {
                                            Config.mcToDcEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("DC → MC"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables sending Discord messages to Minecraft.")))
                                        .binding(Config.dcToMcEnabled, () -> Config.dcToMcEnabled, newVal -> {
                                            Config.dcToMcEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("API URL"))
                                        .description(OptionDescription.of(Text.literal("The URL endpoint for the Discord bot API to receive and send messages.")))
                                        .binding(Config.apiUrl, () -> Config.apiUrl, newVal -> {
                                            Config.apiUrl = newVal;
                                            Config.saveConfig();
                                            WebSocketHandler.initialize();
                                        })
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Screenshots"))
                                .description(OptionDescription.of(Text.literal("Screenshot options")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enabled"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables sending screenshots to Discord.")))
                                        .binding(Config.screenshotsEnabled, () -> Config.screenshotsEnabled, newVal -> {
                                            Config.screenshotsEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Delete After Sending"))
                                        .description(OptionDescription.of(Text.literal("If enabled, deletes the screenshot after sending to Discord.")))
                                        .binding(Config.deleteAfterSending, () -> Config.deleteAfterSending, newVal -> {
                                            Config.deleteAfterSending = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Discord Commands"))
                                .description(OptionDescription.of(Text.literal("Discord command options")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enabled"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables command execution from Discord.")))
                                        .binding(Config.commandExecutionEnabled, () -> Config.commandExecutionEnabled, newVal -> {
                                            Config.commandExecutionEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("/logout"))
                                        .description(OptionDescription.of(Text.literal("Allows or blocks executing the /logout command via Discord.")))
                                        .binding(Config.logoutCommandEnabled, () -> Config.logoutCommandEnabled, newVal -> {
                                            Config.logoutCommandEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("/kill"))
                                        .description(OptionDescription.of(Text.literal("Allows or blocks executing the /kill command via Discord.")))
                                        .binding(Config.killCommandEnabled, () -> Config.killCommandEnabled, newVal -> {
                                            Config.killCommandEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("/screenshot"))
                                        .description(OptionDescription.of(Text.literal("Allows or blocks executing the /screenshot command via Discord.")))
                                        .binding(Config.screenshotCommandEnabled, () -> Config.screenshotCommandEnabled, newVal -> {
                                            Config.screenshotCommandEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("/say"))
                                        .description(OptionDescription.of(Text.literal("Allows or blocks executing the /say command via Discord.")))
                                        .binding(Config.sayCommandEnabled, () -> Config.sayCommandEnabled, newVal -> {
                                            Config.sayCommandEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("/sudo"))
                                        .description(OptionDescription.of(Text.literal("Allows or blocks executing the /sudo command via Discord.")))
                                        .binding(Config.sudoCommandEnabled, () -> Config.sudoCommandEnabled, newVal -> {
                                            Config.sudoCommandEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Client Commands"))
                                .description(OptionDescription.of(Text.literal("Client command options")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enabled"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables client-side commands.")))
                                        .binding(Config.clientCommandsEnabled, () -> Config.clientCommandsEnabled, newVal -> {
                                            Config.clientCommandsEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("/coords"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables executing the /coords command from the client.")))
                                        .binding(Config.coordsCommandEnabled, () -> Config.coordsCommandEnabled, newVal -> {
                                            Config.coordsCommandEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("/afk"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables executing the /afk command from the client.")))
                                        .binding(Config.afkCommandEnabled, () -> Config.afkCommandEnabled, newVal -> {
                                            Config.afkCommandEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Death Messages"))
                                .description(OptionDescription.of(Text.literal("Death message options")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enabled"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables sending death messages to Discord.")))
                                        .binding(Config.deathMessagesEnabled, () -> Config.deathMessagesEnabled, newVal -> {
                                            Config.deathMessagesEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Message Title"))
                                        .description(OptionDescription.of(Text.literal("The title displayed in the webhook message when you die.")))
                                        .binding(Config.deathMessageTitle, () -> Config.deathMessageTitle, newVal -> {
                                            Config.deathMessageTitle = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Coordinates"))
                                        .description(OptionDescription.of(Text.literal("Toggles whether to include player coordinates in the death message.")))
                                        .binding(Config.deathMessageCoordinates, () -> Config.deathMessageCoordinates, newVal -> {
                                            Config.deathMessageCoordinates = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("World Name"))
                                        .description(OptionDescription.of(Text.literal("Toggles whether to include the world name in the death message.")))
                                        .binding(Config.deathMessageWorldName, () -> Config.deathMessageWorldName, newVal -> {
                                            Config.deathMessageWorldName = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Use DM"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables sending death message via direct message.")))
                                        .binding(Config.deathMessageUseDm, () -> Config.deathMessageUseDm, newVal -> {
                                            Config.deathMessageUseDm = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Mention"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables mentioning a specific user on death.")))
                                        .binding(Config.deathMessageMention, () -> Config.deathMessageMention, newVal -> {
                                            Config.deathMessageMention = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("User ID"))
                                        .description(OptionDescription.of(Text.literal("Your User ID, get it by enabling developer mode, right clicking on yourself and then clicking Copy User ID.")))
                                        .binding(Config.deathMessageUserId, () -> Config.deathMessageUserId, newVal -> {
                                            Config.deathMessageUserId = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Color"))
                                        .description(OptionDescription.of(Text.literal("The color of the embed used in death messages.")))
                                        .binding(Color.decode(Config.deathMessageEmbedColor), () -> Color.decode(Config.deathMessageEmbedColor), newVal -> {
                                            Config.deathMessageEmbedColor = "#" + Integer.toHexString(newVal.getRGB()).substring(2);
                                            Config.saveConfig();
                                        })
                                        .controller(ColorControllerBuilder::create)
                                        .build())
                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Player Detector"))
                                .description(OptionDescription.of(Text.literal("Player detector options")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enabled"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables detecting nearby players and sending messages to Discord.")))
                                        .binding(Config.playerDetectionEnabled, () -> Config.playerDetectionEnabled, newVal -> {
                                            Config.playerDetectionEnabled = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Message Title"))
                                        .description(OptionDescription.of(Text.literal("The title used in messages sent when a player is detected.")))
                                        .binding(Config.playerDetectionMessageTitle, () -> Config.playerDetectionMessageTitle, newVal -> {
                                            Config.playerDetectionMessageTitle = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Coordinates"))
                                        .description(OptionDescription.of(Text.literal("Toggles showing the coordinates of the detected player.")))
                                        .binding(Config.playerDetectionMessageCoordinates, () -> Config.playerDetectionMessageCoordinates, newVal -> {
                                            Config.playerDetectionMessageCoordinates = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("World Name"))
                                        .description(OptionDescription.of(Text.literal("Toggles showing the world name of the detected player.")))
                                        .binding(Config.playerDetectionMessageWorldName, () -> Config.playerDetectionMessageWorldName, newVal -> {
                                            Config.playerDetectionMessageWorldName = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Use DM"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables sending player detection messages via direct message.")))
                                        .binding(Config.playerDetectionMessageUseDm, () -> Config.playerDetectionMessageUseDm, newVal -> {
                                            Config.playerDetectionMessageUseDm = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Mention"))
                                        .description(OptionDescription.of(Text.literal("Enables/disables mentioning a user when a player is detected.")))
                                        .binding(Config.playerDetectionMessageMention, () -> Config.playerDetectionMessageMention, newVal -> {
                                            Config.playerDetectionMessageMention = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("User ID"))
                                        .description(OptionDescription.of(Text.literal("Your User ID, get it by enabling developer mode, right clicking on yourself and then clicking Copy User ID.")))
                                        .binding(Config.playerDetectionMessageUserId, () -> Config.playerDetectionMessageUserId, newVal -> {
                                            Config.playerDetectionMessageUserId = newVal;
                                            Config.saveConfig();
                                        })
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Color"))
                                        .description(OptionDescription.of(Text.literal("The color of the embed used in player detection messages.")))
                                        .binding(Color.decode(Config.playerDetectionMessageEmbedColor), () -> Color.decode(Config.playerDetectionMessageEmbedColor), newVal -> {
                                            Config.playerDetectionMessageEmbedColor = "#" + Integer.toHexString(newVal.getRGB()).substring(2);
                                            Config.saveConfig();
                                        })
                                        .controller(ColorControllerBuilder::create)
                                        .build())
                                .build())

                        .build())
                .build().generateScreen(parentScreen);
    }
}
