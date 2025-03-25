package me.sirreal.discordirc.handlers;

import me.sirreal.discordirc.config.Config;
import me.sirreal.discordirc.handlers.WebSocketHandler;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

public class ScreenshotHandler {

    public static void processScreenshot(Framebuffer framebuffer) {
        if (!Config.screenshotsEnabled) {
            return;
        }

        try (NativeImage nativeImage = ScreenshotRecorder.takeScreenshot(framebuffer)) {
            BufferedImage image = convertNativeImageToBufferedImage(nativeImage);
            String base64Screenshot = encodeImageToBase64(image);
            WebSocketHandler.sendScreenshotData(base64Screenshot);

        } catch (IOException e) {
            System.err.println("Error sending screenshot: " + e.getMessage());
        }
    }

    private static BufferedImage convertNativeImageToBufferedImage(NativeImage nativeImage) {
        int width = nativeImage.getWidth();
        int height = nativeImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int bgraColor = nativeImage.getColor(x, y);

                int blue = (bgraColor >> 16) & 0xFF;
                int green = (bgraColor >> 8) & 0xFF;
                int red = bgraColor & 0xFF;
                int alpha = (bgraColor >> 24) & 0xFF;

                int argbColor = (alpha << 24) | (red << 16) | (green << 8) | blue;
                bufferedImage.setRGB(x, y, argbColor);
            }
        }
        return bufferedImage;
    }

    private static String encodeImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
