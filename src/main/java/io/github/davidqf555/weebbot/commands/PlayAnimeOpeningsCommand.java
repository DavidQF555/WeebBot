package io.github.davidqf555.weebbot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.davidqf555.weebbot.Bot;
import io.github.davidqf555.weebbot.GuildInfo;
import io.github.davidqf555.weebbot.Util;
import io.github.davidqf555.weebbot.audio.AnimeOpeningScheduler;
import io.github.davidqf555.weebbot.audio.AudioSendResultHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Map;

public class PlayAnimeOpeningsCommand extends Command {

    @Override
    public void onCommand(Message message, Map<String, String> args) {
        Guild guild = message.getGuild();
        GuildInfo info;
        if (Bot.INFO.containsKey(guild)) {
            info = Bot.INFO.get(guild);
        } else {
            AudioPlayer player = Bot.MANAGER.createPlayer();
            AnimeOpeningScheduler scheduler = new AnimeOpeningScheduler(guild);
            player.addListener(scheduler);
            info = new GuildInfo(player, scheduler);
            Bot.INFO.put(guild, info);
        }
        AudioManager manager = guild.getAudioManager();
        manager.setSendingHandler(new AudioSendResultHandler(info.getPlayer()));
        manager.openAudioConnection(guild.getMember(message.getAuthor()).getVoiceState().getChannel());
        info.getScheduler().loadNextTrack();
    }

    @Override
    public boolean onCheck(Message message, Map<String, String> args) {
        Guild guild = message.getGuild();
        User user = message.getAuthor();
        VoiceChannel channel = guild.getMember(Bot.jda.getSelfUser()).getVoiceState().getChannel();
        VoiceChannel uChannel = guild.getMember(user).getVoiceState().getChannel();
        if (channel != null) {
            if (channel.equals(uChannel)) {
                message.reply(Util.createFailedMessage("Already playing").build()).queue();
            } else {
                message.reply(Util.createFailedMessage("I can only be in one channel").build()).queue();
            }
            return false;
        } else if (uChannel == null) {
            message.reply(Util.createFailedMessage("You must be in a voice channel").build()).queue();
            return false;
        }
        return true;
    }
}
