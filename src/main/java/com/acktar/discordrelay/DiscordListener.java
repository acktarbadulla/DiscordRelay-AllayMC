package com.acktar.discordrelay;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import cn.nukkit.Server;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiscordListener extends ListenerAdapter {
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Ignore bot messages to prevent loops
        if (event.getAuthor().isBot()) {
            return;
        }

        if (DiscordRelay.INSTANCE.CONFIG.discordToMinecraftToggle()) {
        // Get the configured Discord channel ID
        String configuredChannelId = DiscordRelay.INSTANCE.CONFIG.channelId();
        if (configuredChannelId.isEmpty()) {
            log.info("Discord channel ID is not set in the config.");
            return;
        }

        // Get the actual channel from the event
        TextChannel channel = event.getGuild().getTextChannelById(configuredChannelId);
        if (channel == null || !channel.getId().equals(event.getChannel().getId())) {
            return; // Ignore messages from other channels
        }

        // Get the Discord username and message
        String discordUser = event.getAuthor().getName();
        String discordMessage = event.getMessage().getContentDisplay();

        // Get the message format from the config
        String formatTemplate = DiscordRelay.INSTANCE.CONFIG.discordToMinecraftFormat(); // Example: "[Discord | PLAYER] » MESSAGE"

        if (formatTemplate == null || formatTemplate.isEmpty()) {
            log.info("Discord to Minecraft format not set in the config");
            return;
        }

        // Replace placeholders with actual values
        String broadcastMessage = formatTemplate
                .replace("PLAYER", discordUser)
                .replace("MESSAGE", discordMessage);

        // Send the formatted message to all players in the Minecraft server
        Server.getInstance().broadcastMessage(broadcastMessage);
        }
    }
}
