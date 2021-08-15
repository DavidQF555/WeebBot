package io.github.davidqf555.weebbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.davidqf555.weebbot.audio.AudioScheduler;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.Nullable;

public class GuildInfo {

    private final AudioPlayer player;
    private AudioScheduler scheduler;
    private TextChannel channel;

    public GuildInfo(@Nullable AudioScheduler scheduler) {
        player = Bot.MANAGER.createPlayer();
        this.scheduler = scheduler;
        channel = null;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    @Nullable
    public AudioScheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(AudioScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public TextChannel getTextChannel() {
        return channel;
    }

    public void setTextChannel(TextChannel channel) {
        this.channel = channel;
    }

}
