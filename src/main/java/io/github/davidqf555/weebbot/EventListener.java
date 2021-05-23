package io.github.davidqf555.weebbot;

import io.github.davidqf555.weebbot.commands.Command;
import io.github.davidqf555.weebbot.commands.CommandType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class EventListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String raw = message.getContentRaw();
        for (CommandType type : CommandType.values()) {
            Command command = type.getCommand();
            for (String name : type.getNames()) {
                if (raw.toLowerCase().startsWith(Settings.COMMAND + name.toLowerCase())) {
                    if (command.onCheck(message)) {
                        command.onCommand(message);
                        GuildInfo info = Bot.info.get(event.getGuild());
                        if (info != null) {
                            info.setTextChannel(event.getChannel());
                        }
                    }
                    return;
                }
            }
        }
    }
}
