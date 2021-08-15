package io.github.davidqf555.weebbot.commands;

import io.github.davidqf555.weebbot.Bot;
import io.github.davidqf555.weebbot.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Map;

public class LeaveCommand extends Command {

    @Override
    public void onCommand(Message message, Map<String, String> args) {
        message.getGuild().getAudioManager().closeAudioConnection();
    }

    @Override
    public boolean onCheck(Message message, Map<String, String> args) {
        Guild guild = message.getGuild();
        User user = message.getAuthor();
        VoiceChannel channel = guild.getMember(Bot.jda.getSelfUser()).getVoiceState().getChannel();
        if (channel != null && channel.equals(guild.getMember(user).getVoiceState().getChannel())) {
            return true;
        }
        message.reply(Util.createFailedMessage("You must be in the same channel as me").build()).queue();
        return false;
    }

}
