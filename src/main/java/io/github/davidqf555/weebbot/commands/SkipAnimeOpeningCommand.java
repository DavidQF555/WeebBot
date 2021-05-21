package io.github.davidqf555.weebbot.commands;

import io.github.davidqf555.weebbot.Bot;
import io.github.davidqf555.weebbot.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class SkipAnimeOpeningCommand extends Command {

    @Override
    public void onCommand(Message message) {
        Bot.info.get(message.getGuild()).getScheduler().skip();
    }

    @Override
    public boolean onCheck(Message message) {
        Guild guild = message.getGuild();
        User user = message.getAuthor();
        VoiceChannel channel = guild.getMember(Bot.jda.getSelfUser()).getVoiceState().getChannel();
        if (channel == null) {
            message.getChannel().sendMessage(Util.createFailedMessage(user.getAsMention() + " I'm not in a voice channel").build()).queue();
            return false;
        } else if (channel.equals(guild.getMember(message.getAuthor()).getVoiceState().getChannel())) {
            return true;
        }
        message.getChannel().sendMessage(Util.createFailedMessage(user.getAsMention() + " You must be in the same channel as me").build()).queue();
        return false;
    }
}
