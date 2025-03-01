package com.acktar.discordrelay;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

import lombok.extern.slf4j.Slf4j;
import java.util.regex.Pattern;

@Slf4j
public class PlayerListener implements Listener {

    private static final Pattern DISCORD_INVITE_PATTERN = Pattern.compile("(?i)discord\\.gg/[a-zA-Z0-9]+");
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (DiscordRelay.INSTANCE.CONFIG.joinToggle()) {
        // Get the join message from the config
        String joinMessageTemplate = DiscordRelay.INSTANCE.CONFIG.joinMessage();
        
        if (joinMessageTemplate == null || joinMessageTemplate.isEmpty()) {
            log.info("Player JOIN message not set in the config.");
            return;
        }

        // Replace the placeholder "PLAYER" with the actual player's name
        String joinMessage = joinMessageTemplate.replace("PLAYER", event.getPlayer().getName());
        
        // Send message to Discord
        DiscordAPI.sendMessage(joinMessage);
        }
    }

    // Listen to player quitting event
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (DiscordRelay.INSTANCE.CONFIG.quitToggle()) {
                // Get the QUIT message from the config
        String quitMessageTemplate = DiscordRelay.INSTANCE.CONFIG.quitMessage();
        
        if (quitMessageTemplate == null || quitMessageTemplate.isEmpty()) {
            log.info("Player QUIT message not set in the config.");
            return;
        }

        // Replace the placeholder "PLAYER" with the actual player's name
        String quitMessage = quitMessageTemplate.replace("PLAYER", event.getPlayer().getName());;
        
        // Send message to Discord
        DiscordAPI.sendMessage(quitMessage);
        }
    }

    // Listen to player DEATH event
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (DiscordRelay.INSTANCE.CONFIG.deathToggle()) {
        // Get the DEATH message from the config
        String deathMessageTemplate = DiscordRelay.INSTANCE.CONFIG.deathMessage();
        
        if (deathMessageTemplate == null || deathMessageTemplate.isEmpty()) {
            log.info("Player DEATH message not set in the config.");
            return;
        }

        // Replace the placeholder "PLAYER" with the actual player's name
        String deathMessage = deathMessageTemplate.replace("PLAYER", event.getEntity().getName());
        
        // Send message to Discord
        DiscordAPI.sendMessage(deathMessage);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerChat(PlayerChatEvent event) {
        String message = event.getMessage();

        if (DiscordRelay.INSTANCE.CONFIG.minecraftToDiscordToggle()) {
        // Check if the message contains a Discord invite link
        if (DISCORD_INVITE_PATTERN.matcher(message).find()) {
            log.info("Blocked Discord invite from " + event.getPlayer().getName());
            return; // Do not send the message to Discord
        }
        
        // Get the chat message format from the config
        String chatMessageTemplate = DiscordRelay.INSTANCE.CONFIG.minecraftToDiscordFormat(); // Example: "[MC | **PLAYER**] » ** MESSAGE"

        if (chatMessageTemplate == null || chatMessageTemplate.isEmpty()) {
            log.info("Minecraft to Discord Chat message format not set in the config");
            return;
        }

        // Replace placeholders with actual values
        String chatMessage = chatMessageTemplate
            .replace("PLAYER", event.getPlayer().getName())
            .replace("MESSAGE", message);

        // Send the formatted message to Discord
        DiscordAPI.sendMessage(chatMessage);
        }
    }
}
