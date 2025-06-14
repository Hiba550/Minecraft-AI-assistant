package com.quillphen.minecraftaiassistant.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.quillphen.minecraftaiassistant.Constants;
import com.quillphen.minecraftaiassistant.config.AIConfig;
import com.quillphen.minecraftaiassistant.config.ConfigManager;
import com.quillphen.minecraftaiassistant.gemini.GeminiClient;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class AICommandExecutor {
    private static final GeminiClient geminiClient = new GeminiClient();
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("aic")
            .then(Commands.argument("request", StringArgumentType.greedyString())
                .executes(AICommandExecutor::executeAICommandRequest)
            )
        );
    }
    
    private static int executeAICommandRequest(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String request = StringArgumentType.getString(context, "request");
        
        AIConfig config = ConfigManager.getConfig();
        
        if (!config.isConfigured()) {
            source.sendFailure(Component.literal("§c[AI Command] §fAPI key not configured. Press §eK §fto open settings."));
            return 0;
        }
        
        // Check if the source is a player
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("§c[AI Command] §fThis command can only be used by players."));
            return 0;
        }
        
        // Get player's game mode
        GameType gameMode = player.gameMode.getGameModeForPlayer();
        boolean isCreative = gameMode == GameType.CREATIVE;
        boolean isSurvival = gameMode == GameType.SURVIVAL;
        
        // Send "processing..." message
        source.sendSuccess(() -> Component.literal("§b[AI Command] §7Processing request..."), false);
        
        // Create enhanced prompt for command interpretation
        String enhancedPrompt = createCommandPrompt(request, gameMode.getName(), player.getName().getString());
        
        // Call Gemini API asynchronously
        geminiClient.generateResponse(config.getApiKey(), config.getSelectedModel(), enhancedPrompt)
            .thenAccept(response -> {
                handleAIResponse(source, response, player, isCreative, isSurvival, request);
            })
            .exceptionally(throwable -> {
                Constants.LOG.error("Error in AI command executor", throwable);
                source.sendFailure(Component.literal("§c[AI Command] §fAn error occurred while processing your request."));
                return null;
            });
        
        return 1;
    }
    
    private static String createCommandPrompt(String userRequest, String gameMode, String playerName) {
        return String.format("""
            You are a Minecraft AI Command Interpreter. Your job is to convert natural language requests into Minecraft commands.
            
            PLAYER INFO:
            - Name: %s
            - Game Mode: %s
            
            USER REQUEST: "%s"
            
            RESPONSE FORMAT:
            You must respond in this EXACT format:
            
            COMMAND: [minecraft command to execute, or "NONE" if not appropriate]
            MESSAGE: [message to send to player]
            
            RULES:
            1. If game mode is SURVIVAL:
               - NEVER give items, blocks, or XP (no /give, /xp commands)
               - For give requests, respond with: COMMAND: NONE, MESSAGE: Hey %s! You're in survival mode - no cheating allowed! 😄 Try finding or crafting items instead.
               
            2. If game mode is CREATIVE or SPECTATOR:
               - Allow item giving, teleportation, time changes, etc.
               
            3. Common command translations:
               - "make it night" → "time set night"
               - "make it day" → "time set day"
               - "gimme X diamonds" → "give @s minecraft:diamond X" (only in creative)
               - "teleport to spawn" → "tp @s ~ ~ ~" (adjust coordinates)
               - "clear weather" → "weather clear"
               - "rain" → "weather rain"
               
            4. If request is unclear or dangerous, use COMMAND: NONE and explain why.
            
            5. Keep messages friendly and concise.
            
            EXAMPLES:
            Request: "make it night"
            COMMAND: time set night
            MESSAGE: Setting time to night! 🌙
            
            Request: "give me 64 diamonds" (survival mode)
            COMMAND: NONE
            MESSAGE: Hey buddy! You're in survival mode - no cheating allowed! 😄 Try mining for diamonds instead.
            
            Request: "give me 10 iron ingots" (creative mode)
            COMMAND: give @s minecraft:iron_ingot 10
            MESSAGE: Here are your 10 iron ingots! ⚒️
            """, playerName, gameMode, userRequest, playerName);
    }
    
    private static void handleAIResponse(CommandSourceStack source, String aiResponse, ServerPlayer player, boolean isCreative, boolean isSurvival, String originalRequest) {
        try {
            // Parse AI response
            String[] lines = aiResponse.split("\n");
            String command = null;
            String message = null;
            
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("COMMAND:")) {
                    command = line.substring(8).trim();
                } else if (line.startsWith("MESSAGE:")) {
                    message = line.substring(8).trim();
                }
            }
              // Send message to player
            if (message != null && !message.isEmpty()) {
                final String finalMessage = message;
                source.sendSuccess(() -> Component.literal("§b[AI Command] §f" + finalMessage), false);
            }
              // Execute command if provided and valid
            if (command != null && !command.equals("NONE") && !command.isEmpty()) {
                final String finalCommand = command;
                try {
                    // Execute the command
                    source.getServer().getCommands().performPrefixedCommand(source, finalCommand);
                } catch (Exception e) {
                    Constants.LOG.error("Failed to execute AI-generated command: " + finalCommand, e);
                    source.sendFailure(Component.literal("§c[AI Command] §fFailed to execute command: " + finalCommand));
                }
            }
            
        } catch (Exception e) {
            Constants.LOG.error("Failed to parse AI response", e);
            source.sendFailure(Component.literal("§c[AI Command] §fFailed to interpret AI response."));
        }
    }
}
