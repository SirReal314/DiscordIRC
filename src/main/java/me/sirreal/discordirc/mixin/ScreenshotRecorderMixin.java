package me.sirreal.discordirc.mixin;

import me.sirreal.discordirc.handlers.ScreenshotHandler;
import me.sirreal.discordirc.config.Config;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotRecorderMixin {
    @Inject(method = "saveScreenshot", at = @At("HEAD"), cancellable = true)
    private static void onSaveScreenshot(File gameDirectory, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci) {
        if (Config.modEnabled && Config.screenshotsEnabled) {
            ScreenshotHandler.processScreenshot(framebuffer);

            if (Config.deleteAfterSending) {
                ci.cancel();
            }
        }
    }
}
