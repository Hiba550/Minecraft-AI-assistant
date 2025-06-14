package com.quillphen.minecraftaiassistant;

import com.quillphen.minecraftaiassistant.client.MinecraftAIAssistantNeoForgeClient;
import com.quillphen.minecraftaiassistant.command.AICommand;
import com.quillphen.minecraftaiassistant.command.AICommandExecutor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(Constants.MOD_ID)
public class MinecraftAIAssistantNeoForge {

    public MinecraftAIAssistantNeoForge(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        MinecraftAIAssistant.init();
          // Initialize client-side code only on client
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftAIAssistantNeoForgeClient.init();
        }

    }
      @EventBusSubscriber(modid = Constants.MOD_ID)
    public static class ModEvents {
        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            AICommand.register(event.getServer().getCommands().getDispatcher());
            AICommandExecutor.register(event.getServer().getCommands().getDispatcher());
        }
    }
}
