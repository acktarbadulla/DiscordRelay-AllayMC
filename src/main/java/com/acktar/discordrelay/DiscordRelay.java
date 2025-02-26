package com.acktar.discordrelay;

import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.server.Server;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class DiscordRelay extends Plugin {

    public static DiscordRelay INSTANCE;
    public Config CONFIG;

    @Override
    public void onLoad() {
        INSTANCE = this;
        log.info("Loading Configuration file..!");
        CONFIG = ConfigManager.create(Config.class, config -> {
            config.withConfigurer(new YamlSnakeYamlConfigurer());
            config.withBindFile(pluginContainer.dataFolder().resolve("config.yml"));
            config.withRemoveOrphans(true);
            config.saveDefaults();
            config.load(true);
        });
    }

    @Override
    public void onEnable() {
        // Initialize Discord Bot
        DiscordAPI.init();

        // Send "Server Started" message
        if (CONFIG.startMessages()) {
            DiscordAPI.sendMessage(CONFIG.statusServerStarted());
        }
        
        Server.getInstance().getEventBus().registerListener(new PlayerListener());
    }

    @Override
    public void onDisable() {
        // Send "Server Stopped" message
        if (CONFIG.stopMessages()) {
            DiscordAPI.sendMessage(CONFIG.statusServerStopped());
        }

        // Shutdown Discord bot safely
        DiscordAPI.shutdown();
        
        log.info("DiscordRelay disabled!");
    }
}