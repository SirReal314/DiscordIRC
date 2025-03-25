package me.sirreal.discordirc.handlers;

import me.sirreal.discordirc.config.Config;
import me.sirreal.discordirc.util.MinecraftChat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.io.IOException;
import java.net.URI;

public class WebSocketHandler {
    private static WebSocketClient webSocketClient;
    private static boolean isReconnecting = false;
    private static String currentUrl = "ws://" + Config.apiUrl;

    public static void initialize() {
        if (Config.apiUrl == null || Config.apiUrl.isEmpty()) {
            System.err.println("API URL is not valid. Cannot initialize WebSocket.");
            return;
        }

        try {
            String newUrl = "ws://" + Config.apiUrl;
            if (!newUrl.equals(currentUrl)) {
                System.out.println("API URL changed, reconnecting WebSocket...");
                currentUrl = newUrl;
                closeConnection();
            }

            if (webSocketClient != null && webSocketClient.isOpen()) {
                System.out.println("WebSocket is already connected.");
                return;
            }

            URI serverUri = new URI(currentUrl);
            webSocketClient = new WebSocketClient(serverUri) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("WebSocket connected!");
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("Message received: " + message);
                    ChatHandler.handleIncomingMessage(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("WebSocket closed.");
                    handleReconnection();
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("Error: " + ex.getMessage());
                    handleReconnection();
                }
            };
            webSocketClient.connect();
        } catch (Exception e) {
            System.err.println("WebSocket initialization error: " + e.getMessage());
            handleReconnection();
        }
    }

    private static void closeConnection() {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
            webSocketClient = null;
            System.out.println("Closed existing WebSocket connection.");
        }
    }

    private static void handleReconnection() {
        if (isReconnecting) return;

        isReconnecting = true;
        try {
            while (webSocketClient == null || !webSocketClient.isOpen()) {
                Thread.sleep(5000);
                System.out.println("Attempting to reconnect...");
                initialize();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            isReconnecting = false;
        }
    }

    public static boolean sendMessage(String message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(message);
            return true;
        } else {
            System.err.println("WebSocket is not connected.");
            assert MinecraftClient.getInstance().player != null;
            MinecraftChat.sendChatMessage(Text.of("§cFailed to send message. WebSocket not connected."));
            initialize();
            return false;
        }
    }

    public static void sendScreenshotData(String base64Screenshot) throws IOException {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null && webSocketClient != null && webSocketClient.isOpen()) {
            String username = client.player.getName().getString();

            String jsonMessage = String.format("{\"username\": \"%s\", \"message\": \"Screenshot captured\", \"screenshot\": {\"base64\": \"%s\"}}",
                    username, base64Screenshot);

            webSocketClient.send(jsonMessage);
            System.out.println("Screenshot sent to WebSocket");
        } else {
            if (client == null || client.player == null) {
                System.err.println("Player data is not available yet.");
            } else {
                System.err.println("WebSocket is not connected.");
            }
        }
    }
}
