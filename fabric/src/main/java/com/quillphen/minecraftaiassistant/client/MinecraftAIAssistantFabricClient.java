package com.quillphen.minecraftaiassistant.client;

import com.quillphen.minecraftaiassistant.client.gui.AIConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class MinecraftAIAssistantFabricClient implements ClientModInitializer {
    private static KeyMapping configKeyBinding;
    
    @Override
    public void onInitializeClient() {
        // Register key binding for config screen (K key)
        configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.minecraftaiassistant.config",
            GLFW.GLFW_KEY_K,
            "category.minecraftaiassistant"
        ));
          // Register client tick event to handle key presses
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKeyBinding.consumeClick()) {
                client.setScreen(new AIConfigScreen());
            }
        });
    }
}
