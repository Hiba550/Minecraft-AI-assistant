package com.quillphen.minecraftaiassistant.config;

public class AIConfig {
    // Available Gemini Flash models only
    public static final String GEMINI_2_5_FLASH_PREVIEW_05_20 = "gemini-2.5-flash-preview-05-20";
    public static final String GEMINI_2_5_FLASH_PREVIEW_04_17 = "gemini-2.5-flash-preview-04-17";
    public static final String GEMINI_2_0_FLASH_EXP = "gemini-2.0-flash-exp";
    
    private String apiKey = "";
    private String selectedModel = GEMINI_2_5_FLASH_PREVIEW_05_20;
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getSelectedModel() {
        return selectedModel;
    }
    
    public void setSelectedModel(String selectedModel) {
        this.selectedModel = selectedModel;
    }
    
    public boolean isConfigured() {
        return !apiKey.isEmpty();
    }
}
