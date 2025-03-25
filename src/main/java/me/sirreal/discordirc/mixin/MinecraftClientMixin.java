package me.sirreal.discordirc.mixin;

import me.sirreal.discordirc.handlers.EventHandler;
import me.sirreal.discordirc.handlers.ScreenshotHandler;
import me.sirreal.discordirc.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "setScreen", at = @At("HEAD"))
    public void setScreen(Screen screen, CallbackInfo info) {
        if (Config.modEnabled && Config.deathMessagesEnabled && screen instanceof DeathScreen deathScreen) {
            Text deathMessage = ((DeathScreenAccessor) deathScreen).getDeathMessage();
            String deathMessageText = deathMessage.getString();
            MinecraftClient.getInstance().execute(() -> EventHandler.sendDeathMessage(deathMessageText));
        }
    }
}
