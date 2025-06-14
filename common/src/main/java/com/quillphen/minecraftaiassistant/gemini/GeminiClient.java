package com.quillphen.minecraftaiassistant.gemini;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.quillphen.minecraftaiassistant.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class GeminiClient {    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta";
    private static final String SYSTEM_PROMPT = """
        You are a Minecraft AI Assistant! 🎮 Your job is to help with anything Minecraft-related: crafting, mobs, redstone, survival, commands, lore, etc.
        
        IMPORTANT RESPONSE STYLE:
        - Keep responses concise and helpful (max 4-5 lines)
        - Use emojis when appropriate: ⚡🔥🎯💎⚔️🏗️🧙‍♂️
        - Be enthusiastic and friendly
        - Focus on practical, actionable advice
        - Use game terminology
        
        If the question is NOT related to Minecraft, respond humorously like:
        "bruh I'm literally a Minecraft AI 🤖 You asking about [topic]? Go touch some grass lmao 😂"
        
        Example responses:
        - "To craft a furnace: 8 cobblestone in crafting table! 🔥"
        - "Iron spawns Y=0-63, best at Y=15! ⛏️ Bring torches!"
        - "Creepers explode in 1.5 seconds - run or shield! 💥"
        """;
    
    private final HttpClient httpClient;
    private final Gson gson;
    
    public GeminiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.gson = new Gson();
    }
    
    public CompletableFuture<String> generateResponse(String apiKey, String model, String userMessage) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = String.format("%s/models/%s:generateContent?key=%s", BASE_URL, model, apiKey);
                
                JsonObject requestBody = createRequestBody(userMessage);
                
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                        .timeout(Duration.ofSeconds(60))
                        .build();
                
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() != 200) {
                    String errorBody = response.body();
                    Constants.LOG.error("Gemini API error: {} - {}", response.statusCode(), errorBody);
                    
                    if (response.statusCode() == 429) {
                        return "Sorry, I'm being rate limited! Please try again in a moment.";
                    } else if (response.statusCode() == 401) {
                        return "Invalid API key! Please check your configuration.";
                    } else {
                        return "Something went wrong with the AI request. Please try again later.";
                    }
                }
                
                return parseResponse(response.body());
                
            } catch (IOException | InterruptedException e) {
                Constants.LOG.error("Error calling Gemini API", e);
                return "Network error occurred. Please check your connection and try again.";
            } catch (Exception e) {
                Constants.LOG.error("Unexpected error in Gemini API call", e);
                return "An unexpected error occurred. Please try again.";
            }
        });
    }
    
    private JsonObject createRequestBody(String userMessage) {
        JsonObject requestBody = new JsonObject();
        
        // System instruction
        JsonObject systemInstruction = new JsonObject();
        JsonObject systemParts = new JsonObject();
        systemParts.addProperty("text", SYSTEM_PROMPT);
        JsonArray systemPartsArray = new JsonArray();
        systemPartsArray.add(systemParts);
        systemInstruction.add("parts", systemPartsArray);
        requestBody.add("systemInstruction", systemInstruction);
        
        // User content
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        content.addProperty("role", "user");
        
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", userMessage);
        parts.add(part);
        content.add("parts", parts);
        contents.add(content);
        
        requestBody.add("contents", contents);
        
        // Generation config
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 0.7);
        generationConfig.addProperty("maxOutputTokens", 1000);
        requestBody.add("generationConfig", generationConfig);
        
        return requestBody;
    }
    
    private String parseResponse(String responseBody) {
        try {
            JsonObject response = gson.fromJson(responseBody, JsonObject.class);
            
            if (response.has("candidates") && response.getAsJsonArray("candidates").size() > 0) {
                JsonObject candidate = response.getAsJsonArray("candidates").get(0).getAsJsonObject();
                if (candidate.has("content")) {
                    JsonObject content = candidate.getAsJsonObject("content");
                    if (content.has("parts") && content.getAsJsonArray("parts").size() > 0) {
                        JsonObject part = content.getAsJsonArray("parts").get(0).getAsJsonObject();
                        if (part.has("text")) {
                            return part.get("text").getAsString();
                        }
                    }
                }
            }
            
            Constants.LOG.warn("Unexpected response format: {}", responseBody);
            return "Sorry, I couldn't understand the response from the AI service.";
            
        } catch (Exception e) {
            Constants.LOG.error("Error parsing Gemini response", e);
            return "Error parsing AI response. Please try again.";
        }
    }
    
    public void shutdown() {
        // HttpClient resources are managed automatically
    }
}
