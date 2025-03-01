package com.acktar.discordrelay;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class DiscordRelay extends PluginBase {

    public static DiscordRelay INSTANCE;
    public Config CONFIG;

    @Override
    public void onLoad() {
        INSTANCE = this;
        log.info("Loading Configuration file..!");
        CONFIG = ConfigManager.create(Config.class, config -> {
            config.withConfigurer(new YamlSnakeYamlConfigurer());
            config.withBindFile(getDataFolder() + "/config.yml");
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
        
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
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
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("discord")) {
        if (DiscordRelay.INSTANCE.CONFIG.discordCommandToggle()) {
            String discordMessage = DiscordRelay.INSTANCE.CONFIG.discordCommandText();
        if (discordMessage == null || discordMessage.isEmpty()) {
             sender.sendMessage("§cThe Discord message is not set in the config.");
             return false;
        }
        
        sender.sendMessage(discordMessage);
        return true;
        }
        }
        return false;
    }
}
