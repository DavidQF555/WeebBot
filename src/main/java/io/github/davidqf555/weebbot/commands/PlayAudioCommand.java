package io.github.davidqf555.weebbot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.davidqf555.weebbot.Bot;
import io.github.davidqf555.weebbot.GuildInfo;
import io.github.davidqf555.weebbot.Util;
import io.github.davidqf555.weebbot.audio.AudioScheduler;
import io.github.davidqf555.weebbot.audio.AudioSendResultHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Map;

public abstract class PlayAudioCommand extends Command {

    @Override
    public void onCommand(Message message, Map<String, String> args) {
        Guild guild = message.getGuild();
        GuildInfo info = Bot.INFO.computeIfAbsent(guild, g -> new GuildInfo(null));
        AudioScheduler prev = info.getScheduler();
        if (prev != null) {
            prev.end();
        }
        AudioManager manager = guild.getAudioManager();
        AudioPlayer player = info.getPlayer();
        manager.setSendingHandler(new AudioSendResultHandler(player));
        manager.openAudioConnection(guild.getMember(message.getAuthor()).getVoiceState().getChannel());
        AudioScheduler scheduler = createAudioScheduler(guild, args);
        info.setScheduler(scheduler);
        player.addListener(scheduler);
        scheduler.loadNextTrack();
        info.setTextChannel(message.getTextChannel());
    }

    @Override
    public boolean onCheck(Message message, Map<String, String> args) {
        Guild guild = message.getGuild();
        User user = message.getAuthor();
        VoiceChannel channel = guild.getMember(Bot.jda.getSelfUser()).getVoiceState().getChannel();
        VoiceChannel uChannel = guild.getMember(user).getVoiceState().getChannel();
        if (channel != null) {
            if (!channel.equals(uChannel)) {
                message.reply(Util.createFailedMessage("I can only be in one channel").build()).queue();
                return false;
            }
        } else if (uChannel == null) {
            message.reply(Util.createFailedMessage("You must be in a voice channel").build()).queue();
            return false;
        }
        return true;
    }

    protected abstract AudioScheduler createAudioScheduler(Guild guild, Map<String, String> args);

}
