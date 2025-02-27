package com.acktar.discordrelay;

import org.allaymc.api.command.SenderType;
import org.allaymc.api.command.SimpleCommand;
import org.allaymc.api.command.tree.CommandTree;
import org.allaymc.api.entity.interfaces.EntityPlayer;

public class DiscordCommand extends SimpleCommand {

    public DiscordCommand() {
        super("discord", "Get the Discord server link");
    }

    @Override
    public void prepareCommandTree(CommandTree tree) {
        tree.getRoot().exec((context, sender) -> {
            String discordMessage = DiscordRelay.INSTANCE.CONFIG.discordCommandText();

            if (discordMessage == null || discordMessage.isEmpty()) {
                sender.sendText("§cThe Discord message is not set in the config.");
                return context.fail();
            }

            sender.sendText(discordMessage);
            return context.success();
        }, SenderType.ANY);
    }
}
