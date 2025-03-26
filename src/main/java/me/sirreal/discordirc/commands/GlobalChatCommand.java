package me.sirreal.discordirc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.sirreal.discordirc.DiscordIRCClient;
import me.sirreal.discordirc.config.Config;
import me.sirreal.discordirc.util.MinecraftChat;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.text.Text;

public class GlobalChatCommand {
    public static boolean suppressNextMessage = false; // Suppress to format message client-side

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        LiteralCommandNode<FabricClientCommandSource> command = dispatcher.register(ClientCommandManager.literal("global")
                .then(ClientCommandManager.argument("msg", MessageArgumentType.message())
                        .executes(GlobalChatCommand::execute)));
        dispatcher.register(ClientCommandManager.literal("g").redirect(command)); // Shortcut for /g
    }

    private static int execute(CommandContext<FabricClientCommandSource> ctx) {
        String msg = ctx.getArgument("msg", MessageArgumentType.MessageFormat.class).contents();
        assert MinecraftClient.getInstance().player != null;

        if (Config.modEnabled && Config.mcToDcEnabled) {
            MinecraftChat.sendChatMessage(Text.of("§6[Global] §f<" + MinecraftClient.getInstance().player.getName().getString() + "> " + msg));
            suppressNextMessage = true;
        }
        MinecraftClient.getInstance().player.networkHandler.sendChatMessage(msg);
        return 1;
    }
}
