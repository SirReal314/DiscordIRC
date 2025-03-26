package me.sirreal.discordirc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.sirreal.discordirc.config.Config;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.MessageArgumentType;

public class GlobalChatCommand {
    public static boolean isGlobalMessage = false;

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
//            MinecraftChat.sendChatMessage(Text.of("§6[Global] §f<" + MinecraftClient.getInstance().player.getName().getString() + "> " + msg)); // NoChatReports duplicates this. Can't stop it
            isGlobalMessage = true;
        }
        MinecraftClient.getInstance().player.networkHandler.sendChatMessage(msg);
        return 1;
    }
}
