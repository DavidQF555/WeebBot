package io.github.davidqf555.weebbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.github.davidqf555.weebbot.Bot;
import io.github.davidqf555.weebbot.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class AudioScheduler extends AudioEventAdapter {

    private final Guild guild;

    public AudioScheduler(Guild guild) {
        this.guild = guild;
    }

    public void skip() {
        getPlayer().stopTrack();
        loadNextTrack();
    }

    protected abstract String getNextSearch();

    public void loadNextTrack() {
        Bot.MANAGER.loadItem(getNextSearch(), new ResultHandler());
    }

    public AudioPlayer getPlayer() {
        return Bot.INFO.get(guild).getPlayer();
    }

    public TextChannel getTextChannel() {
        return Bot.INFO.get(guild).getTextChannel();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioTrackInfo info = track.getInfo();
        getTextChannel().sendMessage(Util.createMessage("Playing [" + info.title + "](" + info.uri + ")").build()).queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (endReason == AudioTrackEndReason.LOAD_FAILED) {
                AudioTrackInfo info = track.getInfo();
                getTextChannel().sendMessage(Util.createFailedMessage("[" + info.title + "](" + info.uri + ") is failing to load").build()).queue();
            }
            loadNextTrack();
        }
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        AudioTrackInfo info = track.getInfo();
        getTextChannel().sendMessage(Util.createFailedMessage("[" + info.title + "](" + info.uri + ") is failing to provide audio").build()).queue();
        loadNextTrack();
    }

    public void end() {
        Bot.INFO.get(guild).setScheduler(null);
        AudioPlayer player = getPlayer();
        player.stopTrack();
        player.removeListener(this);
    }

    private class ResultHandler implements AudioLoadResultHandler {

        @Override
        public void trackLoaded(AudioTrack track) {
            getPlayer().playTrack(track);
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            trackLoaded(playlist.getTracks().get(0));
        }

        @Override
        public void noMatches() {
            loadNextTrack();
        }

        @Override
        public void loadFailed(FriendlyException exception) {
            loadNextTrack();
        }

    }

}
