package com.acktar.discordrelay;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Config extends OkaeriConfig {

@Comment("--------------------------------------")
@Comment("DISCORD BOT CONFIGURATION")
@Comment("--------------------------------------")

@Comment("DISCORD BOT TOKEN")
@CustomKey("botToken")
private String botToken = "";

@Comment("DISCORD BOT ACTIVITY | EX PLAYING <WITH DISCORDRELAY FOR ALLAYMC>")
@CustomKey("botActivity")
private String botActivity = "WITH DISCORDRELAY FOR ALLAYMC";

@Comment("DISCORD CHANNEL-TOPIC | EX <POWERED BY DISCORDRELAY FOR ALLAYMC>")
@CustomKey("channelTopic")
private String channelTopic = "POWERED BY DISCORDRELAY FOR ALLAYMC";

@Comment("DISCORD CHANNEL-ID FOR SENDING LOGS")
@CustomKey("channelId")
private String channelId = "";

@Comment("--------------------------------------")
@Comment("TOGGLES CONFIGURATION")
@Comment("--------------------------------------")

@Comment("ENABLE SEND MESSAGE TO DISCORD WHEN THE MINECRAFT SERVER STARTS")
@CustomKey("startMessages")
private boolean startMessages = true;

@Comment("ENABLE SEND MESSAGE TO DISCORD WHEN THE MINECRAFT SERVER STOPS")
@CustomKey("stopMessages")
private boolean stopMessages = true;

@Comment("ENABLE SEND MESSAGE TO DISCORD WHEN A PLAYER JOINS THE MINECRAFT SERVER")
@CustomKey("joinToggle")
private boolean joinToggle = true;

@Comment("ENABLE SEND MESSAGE TO DISCORD WHEN A PLAYER LEAVES THE MINECRAFT SERVER")
@CustomKey("quitToggle")
private boolean quitToggle = true;

@Comment("ENABLES MINECRAFT TO DISCORD CHAT RELAY")
@CustomKey("minecraftToDiscordToggle")
private boolean minecraftToDiscordToggle = true;

@Comment("ENABLES DISCORD TO MINECRAFT CHAT RELAY")
@CustomKey("discordToMinecraftToggle")
private boolean discordToMinecraftToggle = true;

@Comment("--------------------------------------")
@Comment("TRANSLATION CONFIGURATION | placeholder: PLAYER")
@Comment("--------------------------------------")

@Comment("MESSAGE THAT IS SENT WHEN THE MINECRAFT SERVER STARTS")
@CustomKey("status_server_started")
private String statusServerStarted = "**:white_check_mark: Server started!**";

@Comment("MESSAGE THAT IS SENT WHEN THE MINECRAFT SERVER STOPS")
@CustomKey("status_server_stopped")
private String statusServerStopped = "**:x: Server stopped!**";

@Comment("MESSAGE THAT IS SENT WHEN A PLAYER JOINS THE MC SERVER")
@CustomKey("joinMessage")
private String joinMessage = "**➕ | PLAYER joined the game!**";

@Comment("MESSAGE THAT IS SENT WHEN A PLAYER LEAVES THE MC SERVER")
@CustomKey("quitMessage")
private String quitMessage = "**➖ | PLAYER left the game!**";

@Comment("MESSAGE FORMAT MINECRAFT TO DISCORD")
@CustomKey("minecraftToDiscordFormat")
private String minecraftToDiscordFormat = "**[MC | PLAYER] »** MESSAGE";

@Comment("MESSAGE FORMAT DISCORD TO MINECRAFT | PLACEHOLDERS: PLAYER, MESSAGE")
@CustomKey("discordToMinecraftFormat")
private String discordToMinecraftFormat = "§l§7[§bDISCORD §7| §r§lPLAYER§7] » §rMESSAGE";

}
