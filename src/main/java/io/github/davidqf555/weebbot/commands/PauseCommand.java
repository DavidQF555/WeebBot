package io.github.davidqf555.weebbot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.davidqf555.weebbot.Bot;
import io.github.davidqf555.weebbot.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Map;

public class PauseCommand extends Command {

    private AudioPlayer player;

    @Override
    public void onCommand(Message message, Map<String, String> args) {
        if (player.isPaused()) {
            message.reply(Util.createMessage("Unpaused Track").build()).queue();
            player.setPaused(false);
        } else {
            message.reply(Util.createMessage("Paused Track").build()).queue();
            player.setPaused(true);
        }
    }

    @Override
    public boolean onCheck(Message message, Map<String, String> args) {
        Guild guild = message.getGuild();
        User user = message.getAuthor();
        VoiceChannel channel = guild.getMember(Bot.jda.getSelfUser()).getVoiceState().getChannel();
        if (channel != null && channel.equals(guild.getMember(user).getVoiceState().getChannel())) {
            if (Bot.INFO.containsKey(guild)) {
                player = Bot.INFO.get(guild).getPlayer();
                return true;
            }
        } else {
            message.reply(Util.createFailedMessage("You must be in the same channel as me").build()).queue();
        }
        return false;
    }
}
