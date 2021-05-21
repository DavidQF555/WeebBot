package io.github.davidqf555.weebbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.davidqf555.weebbot.audio.AnimeOpeningScheduler;
import net.dv8tion.jda.api.entities.TextChannel;

public class GuildInfo {

    private final AudioPlayer player;
    private final AnimeOpeningScheduler scheduler;
    private TextChannel channel;

    public GuildInfo(AudioPlayer player, AnimeOpeningScheduler scheduler) {
        this.player = player;
        this.scheduler = scheduler;
        channel = null;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public AnimeOpeningScheduler getScheduler() {
        return scheduler;
    }

    public TextChannel getTextChannel() {
        return channel;
    }

    public void setTextChannel(TextChannel channel) {
        this.channel = channel;
    }

}
