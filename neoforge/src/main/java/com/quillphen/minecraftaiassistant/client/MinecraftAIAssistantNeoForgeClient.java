package com.quillphen.minecraftaiassistant.client;

import com.quillphen.minecraftaiassistant.Constants;
import com.quillphen.minecraftaiassistant.client.gui.AIConfigScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.lwjgl.glfw.GLFW;

public class MinecraftAIAssistantNeoForgeClient {
    
    private static KeyMapping configKeyBinding;
    
    public static void init() {
        // Register keybind on client side
        NeoForge.EVENT_BUS.register(new ClientEventHandler());
    }
    
    @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModClientEvents {
        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            configKeyBinding = new KeyMapping(
                "key.minecraftaiassistant.config",                
                GLFW.GLFW_KEY_K,
                "category.minecraftaiassistant"
            );
            event.register(configKeyBinding);
        }
    }
    
    public static class ClientEventHandler {
        @SubscribeEvent
        public void onPlayerTick(PlayerTickEvent.Post event) {
            // Only handle on client side
            if (event.getEntity().level().isClientSide) {
                while (configKeyBinding != null && configKeyBinding.consumeClick()) {
                    Minecraft.getInstance().setScreen(new AIConfigScreen());
                }
            }
        }
    }
}
