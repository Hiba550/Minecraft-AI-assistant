package com.quillphen.minecraftaiassistant.client.gui;

import com.quillphen.minecraftaiassistant.config.AIConfig;
import com.quillphen.minecraftaiassistant.config.ConfigManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AIConfigScreen extends Screen {
    private EditBox apiKeyField;
    private CycleButton<String> modelButton;
    private final AIConfig config;
    
    // Layout constants for better spacing
    private static final int PANEL_WIDTH = 320;
    private static final int ELEMENT_HEIGHT = 20;
    private static final int ELEMENT_SPACING = 25;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_SPACING = 10;
    
    public AIConfigScreen() {
        super(Component.literal("AI Assistant Configuration"));
        this.config = ConfigManager.getConfig();
    }
    
    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = this.height / 2 - 60;
        
        // API Key field with proper spacing
        this.apiKeyField = new EditBox(this.font, centerX - PANEL_WIDTH / 2, startY, PANEL_WIDTH, ELEMENT_HEIGHT, Component.literal("API Key"));
        this.apiKeyField.setMaxLength(256);
        this.apiKeyField.setValue(config.getApiKey());
        this.apiKeyField.setHint(Component.literal("Enter your Gemini API key..."));
        this.addRenderableWidget(this.apiKeyField);
        
        // Model selection button with proper spacing
        this.modelButton = CycleButton.<String>builder(text -> Component.literal(getModelDisplayName(text)))
                .withValues(
                    AIConfig.GEMINI_2_5_FLASH_PREVIEW_05_20,
                    AIConfig.GEMINI_2_5_FLASH_PREVIEW_04_17,
                    AIConfig.GEMINI_2_0_FLASH_EXP
                )
                .withInitialValue(config.getSelectedModel())
                .create(centerX - PANEL_WIDTH / 2, startY + ELEMENT_SPACING + 15, PANEL_WIDTH, ELEMENT_HEIGHT, Component.literal("Model"));
        this.addRenderableWidget(this.modelButton);
        
        // Buttons with proper spacing and alignment
        int buttonY = startY + (ELEMENT_SPACING + 15) * 2 + 20;
        
        // Save button
        this.addRenderableWidget(Button.builder(Component.literal("Save Configuration"), button -> {
            config.setApiKey(this.apiKeyField.getValue());
            config.setSelectedModel(this.modelButton.getValue());
            ConfigManager.saveConfig();
            this.onClose();
        }).bounds(centerX - BUTTON_WIDTH - BUTTON_SPACING / 2, buttonY, BUTTON_WIDTH, ELEMENT_HEIGHT).build());
        
        // Cancel button
        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> {
            this.onClose();
        }).bounds(centerX + BUTTON_SPACING / 2, buttonY, BUTTON_WIDTH, ELEMENT_HEIGHT).build());
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        
        int centerX = this.width / 2;
        int startY = this.height / 2 - 60;
        
        // Title with proper positioning
        guiGraphics.drawCenteredString(this.font, this.title, centerX, startY - 35, 0xFFFFFF);
        
        // Field labels with proper spacing
        guiGraphics.drawString(this.font, "Gemini API Key:", centerX - PANEL_WIDTH / 2, startY - 12, 0xFFFFFF);
        guiGraphics.drawString(this.font, "AI Model:", centerX - PANEL_WIDTH / 2, startY + ELEMENT_SPACING + 3, 0xFFFFFF);
        
        // Optional: Add some visual flair with a subtle border
        int panelLeft = centerX - PANEL_WIDTH / 2 - 10;
        int panelRight = centerX + PANEL_WIDTH / 2 + 10;
        int panelTop = startY - 45;
        int panelBottom = startY + (ELEMENT_SPACING + 15) * 2 + 50;
        
        // Draw a subtle border around the configuration panel
        guiGraphics.fill(panelLeft, panelTop, panelRight, panelTop + 1, 0x40FFFFFF); // Top
        guiGraphics.fill(panelLeft, panelBottom - 1, panelRight, panelBottom, 0x40FFFFFF); // Bottom
        guiGraphics.fill(panelLeft, panelTop, panelLeft + 1, panelBottom, 0x40FFFFFF); // Left
        guiGraphics.fill(panelRight - 1, panelTop, panelRight, panelBottom, 0x40FFFFFF); // Right
    }
    
    private String getModelDisplayName(String model) {
        return switch (model) {
            case AIConfig.GEMINI_2_5_FLASH_PREVIEW_05_20 -> "Gemini 2.5 Flash (Latest)";
            case AIConfig.GEMINI_2_5_FLASH_PREVIEW_04_17 -> "Gemini 2.5 Flash (Stable)";
            case AIConfig.GEMINI_2_0_FLASH_EXP -> "Gemini 2.0 Flash (Experimental)";
            default -> model;
        };
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
