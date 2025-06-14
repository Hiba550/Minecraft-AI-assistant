# Minecraft AI Assistant

![Minecraft AI Assistant](https://img.shields.io/badge/Minecraft-1.21.5-green) ![Java](https://img.shields.io/badge/Java-21-orange) ![License](https://img.shields.io/badge/License-MIT-blue)

An intelligent AI-powered assistant mod for Minecraft that integrates Google Gemini 2.5 Flash models to provide helpful responses and command execution assistance directly in-game.

## 🌟 Features

### ✨ Core Functionality
- **AI Chat Commands**: Use `/ai <message>` to chat with the AI assistant
- **Command Assistance**: Use `/aic <request>` to get help with Minecraft commands
- **Multiple AI Models**: Switch between Gemini 2.5 Flash and Gemini 2.5 Flash Thinking models
- **Real-time Responses**: Asynchronous API calls for smooth gameplay experience

### ⚙️ Configuration
- **Easy Setup**: Simple configuration GUI accessible via keybind (default: K key)
- **API Key Management**: Secure storage of your Google AI API key
- **Model Selection**: Choose your preferred Gemini model
- **Persistent Settings**: TOML-based configuration that saves between sessions

### 🎮 User Experience
- **Intuitive Interface**: Clean, modern configuration screen
- **Keybind Support**: Customizable keybinding for quick access
- **Error Handling**: Graceful handling of API errors and invalid configurations
- **Multi-language Ready**: Localization support for different languages

## 🔧 Installation

### Prerequisites
- Minecraft 1.21.5
- Java 21 or higher
- A Google AI API key (get one from [Google AI Studio](https://aistudio.google.com/))

### Mod Loaders Supported
- **Fabric** (with Fabric API)
- **Forge** 
- **NeoForge**

### Steps
1. Download the appropriate jar file for your mod loader from the releases page
2. Place the jar file in your `mods` folder
3. Launch Minecraft
4. Press the default keybind (K) or configure it in Options > Controls > Key Binds > Minecraft AI Assistant
5. Enter your Google AI API key in the configuration screen
6. Start chatting with your AI assistant!

## 🎯 Usage

### Getting Your API Key
1. Visit [Google AI Studio](https://aistudio.google.com/)
2. Sign in with your Google account
3. Navigate to the API keys section
4. Generate a new API key
5. Copy the key and paste it into the mod's configuration screen

### Commands
- **`/ai <message>`** - Chat with the AI assistant
  - Example: `/ai How do I make a iron pickaxe?`
  
- **`/aic <request>`** - Get help with Minecraft commands
  - Example: `/aic How do I teleport to coordinates 100 64 200?`

### Keybinds
- **Open AI Assistant Config** - Default: K key (customizable in controls)

## 🛠️ Configuration

The mod stores its configuration in `config/minecraft-ai-assistant.toml`:

```toml
[ai]
apiKey = "your-api-key-here"
model = "gemini-2.0-flash-exp"  # or "gemini-2.0-flash-thinking-exp"
```

### Available Models
- **gemini-2.0-flash-exp**: Fast, efficient responses for general queries
- **gemini-2.0-flash-thinking-exp**: More thoughtful, detailed responses for complex questions

## 🔧 Development

### Building from Source

#### Prerequisites
- Java 21 JDK
- Git

#### Steps
```bash
git clone https://github.com/yourusername/minecraft-ai-assistant.git
cd minecraft-ai-assistant
./gradlew build
```

#### Running in Development
```bash
# Fabric
./gradlew fabric:runClient

# Forge  
./gradlew forge:runClient

# NeoForge
./gradlew neoforge:runClient
```

### Project Structure
This is a multiloader project using the common/platform architecture:

- **`common/`** - Shared code across all mod loaders
- **`fabric/`** - Fabric-specific implementation
- **`forge/`** - Forge-specific implementation  
- **`neoforge/`** - NeoForge-specific implementation

### Key Components
- **`GeminiClient`** - Handles API communication with Google Gemini
- **`AIConfig`** - Configuration management and TOML serialization
- **`AIConfigScreen`** - GUI for mod configuration
- **`AICommand`** & **`AICommandExecutor`** - Command registration and execution

## 📊 API Usage

The mod uses the Google Gemini API with the following endpoints:
- `https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent`

API calls are made asynchronously to prevent game lag and include proper error handling for:
- Invalid API keys
- Network connectivity issues
- Rate limiting
- Malformed requests

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

### Development Guidelines
- Follow existing code style and conventions
- Test changes on all supported mod loaders
- Update documentation as needed
- Ensure proper error handling

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Google for providing the Gemini AI API
- The Minecraft modding community
- Fabric, Forge, and NeoForge teams for their excellent mod loaders
- MultiLoader Template for the project structure

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/yourusername/minecraft-ai-assistant/issues) page
2. Create a new issue with detailed information about your problem
3. Include your Minecraft version, mod loader, and any error logs

## 🔮 Future Plans

- Integration with additional AI models
- Voice input support
- Advanced command parsing
- Plugin system for custom AI behaviors
- Multiplayer synchronization features

---

**Note**: This mod requires an active internet connection and a valid Google AI API key to function. API usage may be subject to Google's terms of service and rate limits.
