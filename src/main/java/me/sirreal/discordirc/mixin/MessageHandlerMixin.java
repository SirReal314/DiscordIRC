package me.sirreal.discordirc.mixin;

import com.mojang.authlib.GameProfile;
import me.sirreal.discordirc.DiscordIRCClient;
import me.sirreal.discordirc.config.Config;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MessageHandler.class)
public class MessageHandlerMixin {
    // Client side chat handling
    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(SignedMessage message, GameProfile sender, MessageType.Parameters params, CallbackInfo ci) {
        if (Config.modEnabled && Config.mcToDcEnabled) {
            ci.cancel();
        }
    }
}
