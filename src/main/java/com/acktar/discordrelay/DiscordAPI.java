package com.acktar.discordrelay;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InteractionFailureException;
import net.dv8tion.jda.api.requests.GatewayIntent;

import lombok.extern.slf4j.Slf4j;

import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DiscordAPI {
    private static JDA jda;
    
    /**
     * Initializes the Discord bot with the token from config.
     */
    public static void init() {
        String token = DiscordRelay.INSTANCE.CONFIG.botToken();
        if (token.isEmpty()) {
            log.info("Bot token is not set! Discord integration will be disabled.");
            return;
        }

        try {
            jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES).addEventListeners(new DiscordListener()).build().awaitReady();
                    
            // Set presence after the bot is ready
            setPresence(DiscordRelay.INSTANCE.CONFIG.botActivity());
            setChannelTopic();
            
            log.info("Connected to Discord!");
        } catch (Exception e) {
            log.info("Failed to connect to Discord!", e);
        }
    }

    /**
     * Sends a message to the configured Discord channel.
     * @param message The message to send.
     */
    public static void sendMessage(String message) {
        if (jda == null) {
            log.info("JDA is not initialized. Cannot send message.");
            return;
        }

        String channelId = DiscordRelay.INSTANCE.CONFIG.channelId();
        if (channelId.isEmpty()) {
            log.info("Channel ID is not set. Cannot send message.");
            return;
        }

        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) {
            log.info("Invalid Discord channel ID. Cannot send message.");
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                channel.sendMessage(message).queue();
            } catch (InteractionFailureException e) {
                log.info("Failed to send message to Discord!", e);
            }
        });
    }
    
    /**
 * Sets the topic of the channel specified in the config.
 */
public static void setChannelTopic() {
    if (jda == null) {
        log.info("JDA is not initialized. Cannot set channel topic.");
        return;
    }

    String channelId = DiscordRelay.INSTANCE.CONFIG.channelId();
    if (channelId.isEmpty()) {
        log.info("Channel ID is not set. Cannot set channel topic.");
        return;
    }

    TextChannel channel = jda.getTextChannelById(channelId);
    if (channel == null) {
        log.info("Invalid Discord channel ID. Cannot set channel topic.");
        return;
    }

    String topic = DiscordRelay.INSTANCE.CONFIG.channelTopic();
    if (topic == null || topic.isEmpty()) {
        log.info("No topic set in config. Skipping channel topic update.");
        return;
    }

    CompletableFuture.runAsync(() -> {
        try {
            channel.getManager().setTopic(topic).queue(
                success -> log.info("Channel topic updated successfully."),
                failure -> log.info("Failed to update channel topic.", failure)
            );
        } catch (Exception e) {
            log.info("Error while setting channel topic.", e);
        }
    });
}

    /**
     * Shuts down the Discord bot safely.
     */
    public static void shutdown() {
        if (jda != null) {
            jda.shutdown();
            try {
                jda.awaitShutdown(10, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {}
            log.info("Disconnected from Discord.");
        }
    }
    
     /**
     * Set the Discord bot presence
     */  
    public static void setPresence(String status) {
        if (jda == null) {
            log.error("JDA is not initialized! Cannot set presence.");
            return;
        }

        if (status == null || status.isEmpty()) {
            log.info("No bot status set in the config, skipping presence update.");
            return;
        }

        jda.getPresence().setActivity(Activity.playing(status)); // Sets bot's status to "Playing [status]"
        log.info("Bot presence set to: " + status);
    }
}