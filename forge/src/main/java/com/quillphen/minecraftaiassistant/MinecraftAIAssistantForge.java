package com.quillphen.minecraftaiassistant;

import com.quillphen.minecraftaiassistant.client.MinecraftAIAssistantForgeClient;
import com.quillphen.minecraftaiassistant.command.AICommand;
import com.quillphen.minecraftaiassistant.command.AICommandExecutor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class MinecraftAIAssistantForge {

    public MinecraftAIAssistantForge() {
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello Forge world!");
        MinecraftAIAssistant.init();
        
        // Initialize client-side code only on client
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftAIAssistantForgeClient.init();
        });
    }
    
    @Mod.EventBusSubscriber(modid = Constants.MOD_ID)
    public static class ModEvents {
        @SubscribeEvent
        public static void onCommandsRegister(RegisterCommandsEvent event) {
            AICommand.register(event.getDispatcher());
            AICommandExecutor.register(event.getDispatcher());
        }
    }
}
