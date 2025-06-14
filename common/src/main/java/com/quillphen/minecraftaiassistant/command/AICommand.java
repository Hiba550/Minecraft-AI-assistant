package com.quillphen.minecraftaiassistant.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.quillphen.minecraftaiassistant.config.AIConfig;
import com.quillphen.minecraftaiassistant.config.ConfigManager;
import com.quillphen.minecraftaiassistant.gemini.GeminiClient;
import com.quillphen.minecraftaiassistant.Constants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class AICommand {
    private static final GeminiClient geminiClient = new GeminiClient();
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ai")
            .then(Commands.argument("message", StringArgumentType.greedyString())
                .executes(AICommand::executeAICommand)
            )
        );
    }
    
    private static int executeAICommand(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String message = StringArgumentType.getString(context, "message");
        
        AIConfig config = ConfigManager.getConfig();
        
        if (!config.isConfigured()) {
            source.sendFailure(Component.literal("AI Assistant is not configured! Please set your API key using the config menu (press K)."));
            return 0;
        }
        
        // Send "generating..." message
        source.sendSuccess(() -> Component.literal("§6[AI Assistant] §7Generating response..."), false);
        
        // Call Gemini API asynchronously
        geminiClient.generateResponse(config.getApiKey(), config.getSelectedModel(), message)
            .thenAccept(response -> {
                // Send the AI response in chat
                source.sendSuccess(() -> Component.literal("§6[AI Assistant] §f" + response), false);
            })
            .exceptionally(throwable -> {
                Constants.LOG.error("Error in AI command", throwable);
                source.sendFailure(Component.literal("§c[AI Assistant] An error occurred while processing your request."));
                return null;
            });
        
        return 1;
    }
}
