package com.quillphen.minecraftaiassistant;

import com.quillphen.minecraftaiassistant.command.AICommand;
import com.quillphen.minecraftaiassistant.command.AICommandExecutor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class MinecraftAIAssistantFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        MinecraftAIAssistant.init();
          // Register commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            AICommand.register(dispatcher);
            AICommandExecutor.register(dispatcher);
        });
    }
}
