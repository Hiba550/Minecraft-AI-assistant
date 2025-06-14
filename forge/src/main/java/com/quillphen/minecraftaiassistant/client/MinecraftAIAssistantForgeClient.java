package com.quillphen.minecraftaiassistant.client;

import com.quillphen.minecraftaiassistant.Constants;
import com.quillphen.minecraftaiassistant.client.gui.AIConfigScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

public class MinecraftAIAssistantForgeClient {
    
    private static KeyMapping configKeyBinding;    public static void init() {
        // Get the mod event bus and register key mappings
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(MinecraftAIAssistantForgeClient::onRegisterKeyMappings);
        
        // Register client tick event handler on the forge event bus
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
    }
    
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        configKeyBinding = new KeyMapping(
            "key.minecraftaiassistant.config",
            GLFW.GLFW_KEY_K,
            "category.minecraftaiassistant"
        );
        event.register(configKeyBinding);
    }
    
    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                while (configKeyBinding != null && configKeyBinding.consumeClick()) {
                    Minecraft.getInstance().setScreen(new AIConfigScreen());
                }
            }
        }
    }
}
