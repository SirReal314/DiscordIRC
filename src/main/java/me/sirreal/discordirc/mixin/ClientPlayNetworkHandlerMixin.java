package me.sirreal.discordirc.mixin;

import me.sirreal.discordirc.handlers.ChatHandler;
import me.sirreal.discordirc.commands.GlobalChatCommand;
import me.sirreal.discordirc.config.Config;
import me.sirreal.discordirc.util.MinecraftChat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    // Intercept sending a chat message to the server
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        if (GlobalChatCommand.suppressNextMessage) {
            GlobalChatCommand.suppressNextMessage = false;
            return;
        }
        if (Config.modEnabled && Config.mcToDcEnabled) {
            ci.cancel();

            if (ChatHandler.sendMessageToDiscord(message)) {
                assert MinecraftClient.getInstance().player != null;
                MinecraftChat.sendChatMessage(Text.of("§b[Discord] §f<" + MinecraftClient.getInstance().player.getName().getString() + "> " + message));
            }
        }
    }
}
